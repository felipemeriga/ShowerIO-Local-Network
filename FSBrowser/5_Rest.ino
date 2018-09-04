//REST FUNCTIONS .INO FILE
#include <ArduinoJson.h>

void check() {
  String espVersion = "ShowerIO";
  DBG_OUTPUT_PORT.println("The ESP8266 server was discovered by an app");
  String ip = WiFi.localIP().toString();
  DBG_OUTPUT_PORT.println(espVersion);
  DBG_OUTPUT_PORT.println(WiFi.localIP());
  
  String deviceName = checkName();
  String root = "{\"name\": " + deviceName + "," ;
  root = root + "\"ip\": \"" + ip + "\"}" ;

  server.send(200, "application/json", root );
}

void nameYourDevice() {
  DBG_OUTPUT_PORT.println("A new name for this device is being set");
  String showerName = String(server.arg("name"));

  if (createName(showerName)) {
    DBG_OUTPUT_PORT.println("Name: " + showerName + " created successfully!");
    server.send(200, "text/plain", "TRUE");
  } else {
    server.send(500 , "text/plain", "FALSE" );
  }
}


//bool handleFileRead(String path) {
//  DBG_OUTPUT_PORT.println("handleFileRead: " + path);
//  if (path.endsWith("/")) path += "index.html";
//  String contentType = getContentType(path);
//  File file = SPIFFS.open(path, "r");
//  DBG_OUTPUT_PORT.println(contentType);
//  server.streamFile(file, contentType);
//  return true;
//}

void selectDurationTime() {

  DBG_OUTPUT_PORT.println("Set Shower Selected Duration Timer to:" + server.arg("time").toInt());
  DBG_OUTPUT_PORT.println(server.arg("time").toInt());
  EEPROM.write(address_tempo, server.arg("time").toInt());
  EEPROM.commit();
  server.send(204, "");

}

void getDurationTime() {
  DBG_OUTPUT_PORT.println("Getting actual bath duration time");
  String durationTime = String(EEPROM.read(address_tempo));
  DBG_OUTPUT_PORT.println(durationTime);
  server.send(200, "text/plain", durationTime );

}

void setActualShowerTimePlus() {

  DBG_OUTPUT_PORT.println("Set Actual Bath Plus time:" + server.arg("plusTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("plusTime").toInt());
  minutos = minutos + server.arg("plusTime").toInt();
  if (minutos > 45) {
    minutos = 45;
  }
  EEPROM.write(address_tempo, minutos);
  EEPROM.commit();
  server.send(204, "");
}

void setActualShowerTimeLess() {

  DBG_OUTPUT_PORT.println("Set Actual Bath Less time:" + server.arg("lessTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("lessTime").toInt());
  minutos = minutos - server.arg("lessTime").toInt();;
  if (minutos < 1) {
    minutos = 1;
  }
  if (minutos > 45) {
    minutos = 1;
  }
  EEPROM.write(address_tempo, minutos);
  EEPROM.commit();
  server.send(204, "");
}

void selectOffTime() {
  DBG_OUTPUT_PORT.println("Set Selected Actual Off Bath time:" + server.arg("time").toInt());
  DBG_OUTPUT_PORT.println(server.arg("time").toInt());
  minutos_espera = server.arg("time").toInt();
  EEPROM.write(address_espera, minutos_espera);
  EEPROM.commit();
  server.send(204, "");
}

void getOffTime() {
  DBG_OUTPUT_PORT.println("Getting actual bath off time");
  String offTime = String(EEPROM.read(address_espera));
  DBG_OUTPUT_PORT.println(offTime);
  server.send(200, "text/plain", offTime );

}

void setActualOffTimePlus() {

  DBG_OUTPUT_PORT.println("Set Actual  aditional Off time:" + server.arg("plusTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("plusTime").toInt());
  minutos_espera = minutos_espera + server.arg("plusTime").toInt();
  if (minutos_espera > 45) {
    minutos_espera = 45;
  }
  EEPROM.write(address_espera, minutos_espera);
  EEPROM.commit();
  server.send(204, "");
}

void setActualOffTimeLess() {

  DBG_OUTPUT_PORT.println("Set Actual less Off time:" + server.arg("lessTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("lessTime").toInt());
  minutos_pausa = minutos_pausa - server.arg("lessTime").toInt() ;
  if (minutos_pausa < 1) {
    minutos_pausa = 1;
  }
  if (minutos_pausa > 45) {
    minutos_pausa = 1;
  }
  EEPROM.write(address_pausa, minutos_pausa);
  EEPROM.commit();
  server.send(204, "");
}

void selectPausedTime() {
  DBG_OUTPUT_PORT.println("Set Selected Actual Paused Bath time:" + server.arg("time").toInt());
  DBG_OUTPUT_PORT.println(server.arg("time").toInt());
  minutos_pausa = server.arg("time").toInt();
  EEPROM.write(address_pausa, minutos_pausa);
  EEPROM.commit();
  server.send(204, "");
}

void getPausedTime() {
  DBG_OUTPUT_PORT.println("Getting actual bath paused time");
  String pausedTime = String(EEPROM.read(address_pausa));
  DBG_OUTPUT_PORT.println(pausedTime);
  server.send(200, "text/plain", pausedTime );

}

void setActualPausedTimePlus() {

  DBG_OUTPUT_PORT.println("Set Actual  aditional Paused time:" + server.arg("plusTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("plusTime").toInt());
  minutos_pausa = minutos_pausa + server.arg("plusTime").toInt();
  if (minutos_pausa > 45) {
    minutos_pausa = 45;
  }
  EEPROM.write(address_pausa, minutos_pausa);
  EEPROM.commit();
  server.send(204, "");
}

void setActualPausedTimeLess() {

  DBG_OUTPUT_PORT.println("Set Actual less Paused time:" + server.arg("lessTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("lessTime").toInt());
  minutos_pausa = minutos_pausa - server.arg("lessTime").toInt();
  if (minutos_pausa < 1) {
    minutos_pausa = 1;
  }
  if (minutos_pausa > 45) {
    minutos_pausa = 1;
  }
  EEPROM.write(address_pausa, minutos_pausa);
  EEPROM.commit();
  server.send(204, "");
}

void resetWifiManagerSettings() {
  DBG_OUTPUT_PORT.println("The actual saved network will be reseted");
  wifiManager.resetSettings();
  server.send(204, "");
}

void createCredentials() {
  DBG_OUTPUT_PORT.println("A new account is being created");
  String password = String(server.arg("password"));
  String email = String(server.arg("email"));
  if (saveCredentials(email, password)) {
    DBG_OUTPUT_PORT.println("Credentials Saved Successfully!");
    server.send(200, "text/plain", "TRUE");
  } else {
    server.send(500 , "text/plain", "FALSE" );
  }
}

void verifyCredentials() {
  DBG_OUTPUT_PORT.println("Checking credentials");
  String password = String(server.arg("password"));
  String email = String(server.arg("email"));
  String checkAuth = checkCredentials(email, password);
  if (checkAuth != "ERROR") {
    DBG_OUTPUT_PORT.println("Credentials checked successfully!");
    server.send(200, "text/plain", checkAuth);
  } else {
    server.send(500 , "text/plain", checkAuth );
  }
}

void verifyAccountExistance() {
  DBG_OUTPUT_PORT.println("Checking credentials existance");
  String existance = checkExistance();
  server.send(200, "text/plain", existance);
}

void dropCredentials() {
  DBG_OUTPUT_PORT.println("Droping actual credentials");
  dropStoredCredentials();
  server.send(200, "text/plain", "DROPPED");
}






