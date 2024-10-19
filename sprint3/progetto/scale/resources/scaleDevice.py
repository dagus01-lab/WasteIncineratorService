import RPi.GPIO as gpio
import time
from collections import deque
DT =23 #pin 16
SCK=24 #pin 18
calibration=105 #calculate proper calibration as: (raw_value-tare_value)/known_weight
curSample=0
window = deque()
gpio.setwarnings(False)
gpio.setmode(gpio.BCM)
gpio.setup(SCK, gpio.OUT)
gpio.setup(DT, gpio.IN)
def readCount():
  i=0
  Count=0
  while gpio.input(DT) != 0:
    pass

  for _ in range(24):
    gpio.output(SCK,True)
    gpio.output(SCK,False)
    Count <<= 1
    Count |= gpio.input(DT)
    #time.sleep(0.001)
  #print(Count)
  Count=Count ^ 0x800000
  return Count  
def reLU(w):
	if w < 0:
		return 0
	else:
		return w
def readAverage(num_samples=5):
  total = 0
  for _ in range(num_samples):
    total += readCount()
    time.sleep(0.2)  
  return (total / num_samples)

time.sleep(2)
tare= readAverage()
time.sleep(1)
#print("tare successfully calculated!")
while 1:
  curSample= readAverage()
  w=int((curSample-tare)/calibration) 
  w=reLU(-w)
  print(w, flush=True ) #measured in grams
