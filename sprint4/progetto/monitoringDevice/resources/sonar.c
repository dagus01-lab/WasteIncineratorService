#include <gpiod.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>

#define TRIG_PIN 17
#define ECHO_PIN 27
#define CHIP_NAME "gpiochip0"

// Function to sleep for microseconds
void usleep_custom(long microseconds) {
    struct timespec ts;
    ts.tv_sec = microseconds / 1000000;
    ts.tv_nsec = (microseconds % 1000000) * 1000;
    nanosleep(&ts, NULL);
}

// Function to get current time in microseconds
long get_time_microseconds() {
    struct timespec ts;
    clock_gettime(CLOCK_MONOTONIC, &ts);
    return ts.tv_sec * 1000000 + ts.tv_nsec / 1000;
}

// Function to take a single distance sample
float take_sample(struct gpiod_line *trig_line, struct gpiod_line *echo_line) {
    // Send a 10-microsecond pulse on TRIG
    gpiod_line_set_value(trig_line, 1);
    usleep_custom(10);  // 10 microseconds
    gpiod_line_set_value(trig_line, 0);

    // Wait for ECHO to go high and record the start time
    long pulse_start = get_time_microseconds();
    while (gpiod_line_get_value(echo_line) == 0) {
        pulse_start = get_time_microseconds();
    }

    // Wait for ECHO to go low and record the end time
    long pulse_end = get_time_microseconds();
    while (gpiod_line_get_value(echo_line) == 1) {
        pulse_end = get_time_microseconds();
    }

    // Calculate pulse duration and distance
    long pulse_duration = pulse_end - pulse_start;
    float distance = pulse_duration * 0.017165;  // distance = (speed of sound * time) / 2
    return distance;
}

// Function to calculate average distance from multiple samples
float take_average(struct gpiod_line *trig_line, struct gpiod_line *echo_line, int num_samples) {
    float total = 0;
    for (int i = 0; i < num_samples; i++) {
        total += take_sample(trig_line, echo_line);
        usleep_custom(150000);  // Wait 150 milliseconds between samples
    }
    return total / num_samples;
}

int main() {
    // Open the GPIO chip
    struct gpiod_chip *chip = gpiod_chip_open_by_name(CHIP_NAME);
    if (!chip) {
        perror("Failed to open GPIO chip");
        return 1;
    }

    // Get TRIG and ECHO lines
    struct gpiod_line *trig_line = gpiod_chip_get_line(chip, TRIG_PIN);
    struct gpiod_line *echo_line = gpiod_chip_get_line(chip, ECHO_PIN);
    if (!trig_line || !echo_line) {
        perror("Failed to get GPIO lines");
        gpiod_chip_close(chip);
        return 1;
    }

    // Configure TRIG as output and ECHO as input
    if (gpiod_line_request_output(trig_line, "sonar_trig", 0) < 0) {
        perror("Failed to request TRIG line as output");
        gpiod_chip_close(chip);
        return 1;
    }
    if (gpiod_line_request_input(echo_line, "sonar_echo") < 0) {
        perror("Failed to request ECHO line as input");
        gpiod_chip_close(chip);
        return 1;
    }

    // Main loop to print distance measurements
    while (1) {
        float distance = take_average(trig_line, echo_line, 5);
        //printf("Distance: %.1f cm\n", distance);
	    printf("%.1f\n", distance);
        fflush(stdout);
        usleep_custom(1000000);  // Wait 1 second before the next reading
    }

    // Clean up and close the chip
    gpiod_line_release(trig_line);
    gpiod_line_release(echo_line);
    gpiod_chip_close(chip);
    return 0;
}