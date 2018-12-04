#include <Adafruit_NeoPixel.h>

#define PIN13 13
#define PIN12 12
#define PIN11 11
#define PIN10 10
#define PIN9 9
#define PIN8 8
#define PIN7 7
#define PIN6 6
#define PIN5 5

#define LIGHT_COUNT 12
#define MAX_RGB_VAL 255

Adafruit_NeoPixel stripList[9] = {
  Adafruit_NeoPixel(LIGHT_COUNT, PIN13, NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(LIGHT_COUNT, PIN12, NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(LIGHT_COUNT, PIN11, NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(LIGHT_COUNT, PIN10, NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(LIGHT_COUNT, PIN9, NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(LIGHT_COUNT, PIN8, NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(LIGHT_COUNT, PIN7, NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(LIGHT_COUNT, PIN6, NEO_GRB + NEO_KHZ800),
  Adafruit_NeoPixel(LIGHT_COUNT, PIN5, NEO_GRB + NEO_KHZ800)
};

int r = 1, g = 1, b = 1;
int incr = r, incg = g, incb = b;

int singleIncre = 0;
boolean listenToSerial = false;
      
void setup() {
  Serial.begin(2000000);
  Serial.setTimeout(1);
  for (int y = 0; y < 9; y ++) {
    stripList[y].begin();
    stripList[y].show(); // Initialize all pixels to 'off'
  }
}

void loop() {
  for (int y = 0; y < 9; y ++) {
    for (int x = 0; x < LIGHT_COUNT; x ++) {
      if (listenToSerial) {
        int incomingIntR = Serial.parseInt();
        int incomingIntG = Serial.parseInt();
        int incomingIntB = Serial.parseInt();
        stripList[y].setPixelColor(x, incomingIntR, incomingIntG, incomingIntB);
      } else {
        pulseWhite();
        stripList[y].setPixelColor(x, r, g, b);
      }
      stripList[y].show();
    }
    delay(1);
  }
}

void rainBowCycle(int increm, int* colorR, int* colorG, int* colorB) {
  
  return;
}

int wheelCycle(int index) {
  if (index >= (MAX_RGB_VAL * 2)) {
    int difference = index - (MAX_RGB_VAL * 2);
    return difference;
  }
  if (index >= MAX_RGB_VAL) {
    int difference = MAX_RGB_VAL - (index - MAX_RGB_VAL);
    return difference;
  }
  index ++;
  return index;
}

int wheelIncremeant(int index) {
  if (index >= MAX_RGB_VAL) {
    return 0;
  }
  index ++;
  return index;
}

int wheelDecremeant(int index) {
  if (index <= 0) {
    return 0;
  }
  index --;
  return index;
}

void pulseWhite() {
  r = wheelCycle(incr);
  g = wheelCycle(incg);
  b = wheelCycle(incb);
  if (incr > 0) {
    incr ++;
  } else {
    r = 0;
    if (incg > 0) {
      incg ++;
    } else {
      g = 0;
      if (incb > 0) {
        incb ++;
      } else {
        b = 0;
        listenToSerial = true;
      }
    } 
  }
  if (incr >= (MAX_RGB_VAL * 2)) {
    incr = 0;
  }
  if (incg >= (MAX_RGB_VAL * 2)) {
    incg = 0;
  }
  if (incb >= (MAX_RGB_VAL * 2)) {
    incb = 0;
  }
  delay(3);
}

void crazyColor() {
  r = wheelCycle(incr);
  g = wheelCycle(incg);
  b = wheelCycle(incb);
  incr += random(12);
  incg += random(12);
  incb += random(12);
  if (incr >= (MAX_RGB_VAL * 2)) {
    int difference = incr - (MAX_RGB_VAL * 2);
    incr = difference;
  }
  if (incg >= (MAX_RGB_VAL * 2)) {
    int difference = incg - (MAX_RGB_VAL * 2);
    incg = difference;
  }
  if (incb >= (MAX_RGB_VAL * 2)) {
    int difference = incb - (MAX_RGB_VAL * 2);
    incb = difference;
  }
}

