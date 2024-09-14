import RPi.GPIO as gpio
import time
from collections import deque
DT =23 #pin 16
SCK=24 #pin 18
calibration=50 #calculate proper calibration as: (raw_value-tare_value)/known_weight
curSample=0
window = deque()
gpio.setwarnings(False)
gpio.setmode(gpio.BCM)
gpio.setup(SCK, gpio.OUT)

def readCount():
  i=0
  Count=0
  gpio.setup(DT, gpio.OUT)
  gpio.output(DT,1)
  gpio.output(SCK,0)
  gpio.setup(DT, gpio.IN)
  while gpio.input(DT) == 1:
    i=0
    for i in range(24):
      gpio.output(SCK,1)
      Count=Count<<1
      gpio.output(SCK,0)
      #time.sleep(0.001)
      if gpio.input(DT) == 0: 
        Count=Count+1
        #print Count

  gpio.output(SCK,1)
  Count=Count^0x800000
  gpio.output(SCK,0)
  return Count  

def readAverage(num_samples=5):
  total = 0
  for _ in range(num_samples):
    total += readCount()
    time.sleep(0.01)  
  return total / num_samples

def movingAverage(new_value, window, window_size=20):
    window.append(new_value)
    if len(window) > window_size:
        window.popleft()
    return sum(window) / len(window)

time.sleep(2)
tare= readAverage()
#print("tare successfully calculated!")
while 1:
  curSample= readAverage()
  curSampleSmoothed= movingAverage(curSample, window)
  w=(curSampleSmoothed-tare)/calibration 
  print(w, flush=True ) #measured in grams
  time.sleep(0.25)
