# File: sonar.py
import RPi.GPIO as GPIO
import time
 

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
TRIG = 17
ECHO = 27

GPIO.setup(TRIG,GPIO.OUT)
GPIO.setup(ECHO,GPIO.IN)

GPIO.output(TRIG, False)   #TRIG parte LOW
#print ('Waiting a few seconds for the sensor to settle')
time.sleep(2)

def takeSample():
   GPIO.output(TRIG, True)    #invia impulsoTRIG
   time.sleep(0.00001)
   GPIO.output(TRIG, False)

   #attendi che ECHO parta e memorizza tempo
   while GPIO.input(ECHO)==0:
      pulse_start = time.time()

   # register the last timestamp at which the receiver detects the signal.
   while GPIO.input(ECHO)==1:
      pulse_end = time.time()

   pulse_duration = pulse_end - pulse_start
   distance = pulse_duration * 17165   #distance = vt/2
   return round(distance, 1)

def takeAverage(num_samples=5):
  total = 0
  for _ in range(num_samples):
    total += takeSample()
    time.sleep(0.01)  
  return total / num_samples

while True:
   distance = takeAverage()
   #print ('Distance:',distance,'cm')distance
   print ( distance, flush=True) 
   time.sleep(0.25)


#GPIO.cleanup()