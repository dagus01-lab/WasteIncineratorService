#include <gpiod.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <pthread.h>

#define LED_PIN 25

volatile int blinking = 0;
volatile int stop_thread = 0;
struct gpiod_line *led_line;
struct gpiod_chip *chip;

void signal_handler(int sig) {
    printf("Exiting...\n");
    blinking = 0;
    stop_thread = 1;
    gpiod_line_set_value(led_line, 0); // Turn off the LED before exiting
    exit(0);
}

void* blink_led(void* arg) {
    while (blinking) {
        if (stop_thread) {
            break;
        }
        gpiod_line_set_value(led_line, 1); // Turn LED ON
        usleep(500000); // Sleep for 500ms
        if (blinking) {
            gpiod_line_set_value(led_line, 0); // Turn LED OFF
            usleep(500000); // Sleep for 500ms
        }
    }
    return NULL;
}

int main() {
    signal(SIGINT, signal_handler);

    chip = gpiod_chip_open_by_name("gpiochip0"); // Use the appropriate GPIO chip
    if (!chip) {
        perror("Failed to open GPIO chip");
        return EXIT_FAILURE;
    }

    led_line = gpiod_chip_get_line(chip, LED_PIN);
    if (!led_line) {
        perror("Failed to get LED line");
        gpiod_chip_close(chip);
        return EXIT_FAILURE;
    }

    if (gpiod_line_request_output(led_line, "LedDevice", 0) < 0) {
        perror("Failed to request LED line as output");
        gpiod_chip_close(chip);
        return EXIT_FAILURE;
    }

    char input[10];
    while (fgets(input, sizeof(input), stdin)) {
        input[strcspn(input, "\n")] = 0; // Remove newline
        printf("LedDevice receives: %s\n", input);
        if (strstr(input, "on") != NULL) {
            blinking = 0;
            gpiod_line_set_value(led_line, 1); // Turn LED ON
        } else if (strstr(input, "off") != NULL) {
            blinking = 0;
            gpiod_line_set_value(led_line, 0); // Turn LED OFF
        } else if (strstr(input, "blink") != NULL) {
            if (!blinking) {
                blinking = 1;
                pthread_t blink_thread;
                pthread_create(&blink_thread, NULL, blink_led, NULL);
            }
        } else {
            printf("LedDevice | Unknown command\n");
            blinking = 0;
        }
    }

    gpiod_line_release(led_line); // Clean up
    gpiod_chip_close(chip);
    return EXIT_SUCCESS;
}