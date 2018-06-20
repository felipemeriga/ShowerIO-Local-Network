//DEFINES AND FUNCTION PROTOTYPES

#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include <FS.h>
#include <DNSServer.h>
#include <EEPROM.h>
#include <Ticker.h> 
#include <WiFiManager.h>


//const byte DNS_PORT = 53;
MDNSResponder mdns;
WiFiManager wifiManager;

#define buttonPin D2  // the number of the pushbutton pin
#define rele D1      // the number of the LED pin
#define Led_Aviso D0

Ticker botao;
Ticker tyme;

bool shouldSaveConfig = false;

// Variables will change:
int releState = LOW;         // the current state of the output pin
int buttonState;             // the current reading from the input pin
int lastButtonState = LOW;   // the previous reading from the input pin
int led_S = LOW;


unsigned long lastDebounceTime = 0;  // the last time the output pin was toggled
unsigned long debounceDelay = 25;    // the debounce time; increase if the output flickers

enum Estado_Botao {
  desligado,
  iniciar_banho,
  pausar_banho,
  continuar_banho
};


enum Estado_Banho {
  habilitado,
  desabilitado
};

Estado_Botao Estado_Bot = desligado;
Estado_Banho Banho = habilitado;


//posiçoes para armazenar variaveis na EEPROM
int address_tempo = 0;
int address_espera = 1;
int address_pausa = 2;


byte armazenado;
byte minutos = EEPROM.read(address_tempo); //tempo de banho
byte minutos_espera = EEPROM.read(address_espera); //tempo de espera até o banho ser habilitado novamente
byte minutos_pausa = EEPROM.read(address_pausa); // tempo que o banho pode ficar pausado
int tempo = (int)minutos * 60;
int tempo_espera = (int)minutos_espera * 60;
int tempo_de_pausa = (int)minutos_pausa * 60;

#define DBG_OUTPUT_PORT Serial
//DNSServer dnsServer;
const char* ssid = "GVT_666";
const char* password = "meleka123";
const char* host = "esp8266fs";

ESP8266WebServer server(80);
//holds the current upload
File fsUploadFile;


//Function prototypes
//FS Functions
String formatBytes(size_t bytes);
String getContentType(String filename);

//API REST Mapping Functions
bool handleFileRead(String path);
void selectDurationTime();
void setActualShowerTimePlus();
void setActualShowerTimeLess();
void selectOffTime();
void setActualOffTimePlus();
void setActualOffTimeLess();
void selectPausedTime();
void setActualPausedTimePlus();
void setActualPausedTimeLess();

//Shower Logic Functions
void logica_botao();
void logica_tempo();


