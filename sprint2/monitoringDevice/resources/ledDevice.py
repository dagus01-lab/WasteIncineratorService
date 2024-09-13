# File: LedDevice.py
import RPi.GPIO as GPIO
import sys
import time
import threading

# Setup GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setup(25, GPIO.OUT)

blinking = False

def blink_led():
    while blinking:
        GPIO.output(25, GPIO.HIGH)
        time.sleep(0.5)
        GPIO.output(25, GPIO.LOW)
        time.sleep(0.5)

for line in sys.stdin:
    line = line.strip()
    print("LedDevice receives:", line)
    try:
        if 'on' in line:
            # Turn on the LED
            GPIO.output(25, GPIO.HIGH)
            blinking = False 
        elif 'off' in line:
            # Turn off the LED
            GPIO.output(25, GPIO.LOW)
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