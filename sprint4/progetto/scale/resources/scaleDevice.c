#include <gpiod.h>
#include <stdio.h>
#include <unistd.h>
#include <time.h>
#include <stdint.h>

#define DT_PIN 23        // Data pin, GPIO 23 (BCM numbering)
#define SCK_PIN 24       // Clock pin, GPIO 24 (BCM numbering)
#define CALIBRATION 105  // Calibration factor, adjust as needed

struct gpiod_chip *chip;
struct gpiod_line *data_line;
struct gpiod_line *sck_line;

void delay_ms(int milliseconds) {
    struct timespec ts;
    ts.tv_sec = milliseconds / 1000;
    ts.tv_nsec = (milliseconds % 1000) * 1000000;
    nanosleep(&ts, NULL);
}

int32_t read_count() {
    int32_t count = 0;
    int i;

    // Wait for DT line to go LOW
    while (gpiod_line_get_value(data_line) != 0) {}

    // Read 24-bit data
    for (i = 0; i < 24; i++) {
        gpiod_line_set_value(sck_line, 1);  // Pulse SCK HIGH
        count <<= 1;
        gpiod_line_set_value(sck_line, 0);  // Pulse SCK LOW
        count |= gpiod_line_get_value(data_line);  // Read DT
    }

    // Apply two's complement for signed result
    count ^= 0x800000;

    return count;
}

int32_t relu(int32_t weight) {
    return weight < 0 ? 0 : weight;
}

double read_average(int num_samples) {
    int64_t total = 0;
    for (int i = 0; i < num_samples; i++) {
        total += read_count();
        delay_ms(200);
    }
    return (double)total / num_samples;
}

int main() {
    chip = gpiod_chip_open_by_name("gpiochip0");
    if (!chip) {
        perror("Failed to open GPIO chip");
        return -1;
    }

    // Initialize DT and SCK lines
    data_line = gpiod_chip_get_line(chip, DT_PIN);
    sck_line = gpiod_chip_get_line(chip, SCK_PIN);
    if (!data_line || !sck_line) {
        perror("Failed to get GPIO line");
        gpiod_chip_close(chip);
        return -1;
    }

    if (gpiod_line_request_input(data_line, "weight_sensor") < 0 ||
        gpiod_line_request_output(sck_line, "weight_sensor", 0) < 0) {
        perror("Failed to request GPIO line");
        gpiod_chip_close(chip);
        return -1;
    }

    delay_ms(2000);  // Allow the sensor to stabilize

    // Tare measurement
    double tare = read_average(5);
    delay_ms(1000);

    // Continuous reading loop
    while (1) {
        int32_t cur_sample = read_average(5);
        int32_t weight = (int)((cur_sample - tare) / CALIBRATION);
        weight = relu(-weight);  // Apply ReLU
        printf("%d\n", weight);  // Output weight in grams
        fflush(stdout);          // Ensure output is printed immediately
        delay_ms(250);
    }

    // Cleanup
    gpiod_line_release(data_line);
    gpiod_line_release(sck_line);
    gpiod_chip_close(chip);
    return 0;
}
