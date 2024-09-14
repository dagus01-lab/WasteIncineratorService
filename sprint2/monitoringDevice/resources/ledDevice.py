# File: LedDevice.py
import RPi.GPIO as GPIO
import sys
import time
import threading
import signal

# Setup GPIO
LED=25
GPIO.setmode(GPIO.BCM)
GPIO.setup(LED, GPIO.OUT)

# Signal handler to catch Ctrl+C and clean up
def signal_handler(sig, frame):
    global blinking, stop_thread, LED
    print("Exiting...")

    blinking = False
    stop_thread = True
    GPIO.output(LED, GPIO.LOW)  # Turn off the LED before exiting
    GPIO.cleanup() 
    sys.exit(0) 

# Register the signal handler
signal.signal(signal.SIGINT, signal_handler)

# Led blinking thread setup
blinking = False
stop_thread = False 

def blink_led():
    while blinking:
        if stop_thread:
            break
        GPIO.output(LED, GPIO.HIGH)
        time.sleep(0.5)
        GPIO.output(LED, GPIO.LOW)
        time.sleep(0.5)

for line in sys.stdin:
    line = line.strip()
    print("LedDevice receives:", line)
    try:
        if 'on' in line:
            # Turn on the LED
            GPIO.output(LED, GPIO.HIGH)
            blinking = False
        elif 'off' in line:
            # Turn off the LED
            GPIO.output(LED, GPIO.LOW)
            blinking = False
        elif 'blink' in line:
            # Start blinking the LED
            if not blinking:
                blinking = True
                blink_thread = threading.Thread(target=blink_led)
                blink_thread.start()
        else:
            print("LedDevice | Unknown command")
            blinking = False
    except Exception as e:
        print(f"LedDevice | An exception occurred: {e}")