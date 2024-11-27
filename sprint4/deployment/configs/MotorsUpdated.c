#include <gpiod.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

// GPIO pin constants
#define CHIP_NAME "/dev/gpiochip0"
#define IN1 3       // BCM 3
#define IN2 2       // BCM 2
#define IN3 9       // BCM 10
#define IN4 10      // BCM 9

// Duration constants
#define SLEEP_DURATION 1000000
#define MOVE_PAUSE_DURATION 400000
 
// PWM period (in microseconds)
#define PWM_PERIOD 36000  // 35 ms for PWM period

// Function to set motor direction with PWM for speed control
void set_motor_speed(struct gpiod_line *in1, struct gpiod_line *in2, struct gpiod_line *in3, struct gpiod_line *in4,
                     int a, int b, int c, int d, int duty_cycle) {
    int on_time = (PWM_PERIOD * duty_cycle) / 100;       // High time based on duty cycle
    int off_time = PWM_PERIOD - on_time;                 // Low time based on duty cycle

    for (int i = 0; i < (SLEEP_DURATION / PWM_PERIOD); i++) {
        // Set GPIO values for the on period
        gpiod_line_set_value(in1, a);
        gpiod_line_set_value(in2, b);
        gpiod_line_set_value(in3, c);
        gpiod_line_set_value(in4, d);
        usleep(on_time);

        // Set GPIO values to 0 for the off period
        gpiod_line_set_value(in1, 0);
        gpiod_line_set_value(in2, 0);
        gpiod_line_set_value(in3, 0);
        gpiod_line_set_value(in4, 0);
        usleep(off_time);
    }
}

// Forward, backward, left, and right functions with speed control
void forward(struct gpiod_line *in1, struct gpiod_line *in2, struct gpiod_line *in3, struct gpiod_line *in4, int speed) {
    printf("Moving forward\n");
    set_motor_speed(in1, in2, in3, in4, 1, 0, 1, 0, speed);
//    usleep(SLEEP_DURATION);
}

void backward(struct gpiod_line *in1, struct gpiod_line *in2, struct gpiod_line *in3, struct gpiod_line *in4, int speed) {
    printf("Moving backward\n");
    set_motor_speed(in1, in2, in3, in4, 0,1,0,1, speed);
//    usleep(SLEEP_DURATION);
}

void left(struct gpiod_line *in1, struct gpiod_line *in2, struct gpiod_line *in3, struct gpiod_line *in4, int speed) {
    printf("Turning left\n");
    set_motor_speed(in4, in3, in1, in2, 0, 1, 0, 1, speed);
//    usleep(SLEEP_DURATION);
}

void right(struct gpiod_line *in1, struct gpiod_line *in2, struct gpiod_line *in3, struct gpiod_line *in4, int speed) {
    printf("Turning right\n");
    set_motor_speed(in1, in2, in3, in4, 1, 0, 0, 1, speed);
//    usleep(SLEEP_DURATION);
}

// Stop function
void halt(struct gpiod_line *in1, struct gpiod_line *in2, struct gpiod_line *in3, struct gpiod_line *in4) {
    printf("Halting motors\n");
    gpiod_line_set_value(in1, 0);
    gpiod_line_set_value(in2, 0);
    gpiod_line_set_value(in3, 0);
    gpiod_line_set_value(in4, 0);
}

// Main function
int main() {
    struct gpiod_chip *chip;
    struct gpiod_line *in1, *in2, *in3, *in4;
    char command, last_command = 'l';
    int straightSpeed = 20;
    int leftRotationSpeed = 17;
    int rightRotationSpeed = 17;
    // Open GPIO chip
    chip = gpiod_chip_open(CHIP_NAME);
    if (!chip) {
        perror("Failed to open GPIO chip");
        return 1;
    }

    // Configure GPIO lines
    in1 = gpiod_chip_get_line(chip, IN1);
    in2 = gpiod_chip_get_line(chip, IN2);
    in3 = gpiod_chip_get_line(chip, IN3);
    in4 = gpiod_chip_get_line(chip, IN4);

    if (!in1 || !in2 || !in3 || !in4) {
        perror("Failed to get GPIO line");
        gpiod_chip_close(chip);
        return 1;
    }

    gpiod_line_request_output(in1, "motor-control", 0);
    gpiod_line_request_output(in2, "motor-control", 0);
    gpiod_line_request_output(in3, "motor-control", 0);
    gpiod_line_request_output(in4, "motor-control", 0);
    
    left(in1, in2, in3, in4, leftRotationSpeed);
    halt(in1, in2, in3, in4);
    usleep(MOVE_PAUSE_DURATION);
    right(in1, in2, in3, in4, rightRotationSpeed);
    halt(in1, in2, in3, in4);
    usleep(MOVE_PAUSE_DURATION);
    printf("Controls: w-forward s-backward a-left d-right h-halt e-exit\n");
    printf("Use + to increase speed and - to decrease speed\n");

    while (1) {
        command = getchar();
        printf("Last command: %c, Executing command: %c\n", last_command, command);
        if(command != last_command && (command == 'r' || command == 'l' || command == 'w')){
            usleep(MOVE_PAUSE_DURATION);
            last_command = command;
        }
        switch (command) {
            case 'w':
                forward(in1, in2, in3, in4, straightSpeed);
                halt(in1, in2, in3, in4);
                break;
            case 's':
                backward(in1, in2, in3, in4, straightSpeed);
                halt(in1, in2, in3, in4);
                break;
            case 'l':
                left(in1, in2, in3, in4, leftRotationSpeed);
                halt(in1, in2, in3, in4);
                break;
            case 'r':
                right(in1, in2, in3, in4, rightRotationSpeed);
                halt(in1, in2, in3, in4);
                break;
            case '+':
                straightSpeed = (straightSpeed < 100) ? straightSpeed + 10 : 100;
                printf("Speed increased to %d%%\n", straightSpeed);
                break;
            case '-':
                straightSpeed = (straightSpeed > 0) ? straightSpeed - 10 : 0;
                printf("Speed decreased to %d%%\n", straightSpeed);
                break;
            case 'h':
                halt(in1, in2, in3, in4);
                break;
            case 'e':
                halt(in1, in2, in3, in4);
                gpiod_chip_close(chip);
                return 0;
            default:
                printf("Unknown command\n");
                break;
        }
    }

    // Close GPIO chip
    gpiod_chip_close(chip);
    return 0;
}
