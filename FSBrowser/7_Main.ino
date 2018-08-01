//Main Function .INO FILE

void setup(void) {

  Serial.begin(115200);
  Serial.print("FIRST");

  //Wifi Manager Configuration
  //wifiManager.setSTAStaticIPConfig(IPAddress(192, 168, 0, 99), IPAddress(192, 168, 0, 1), IPAddress(255, 255, 255, 0));
  //wifiManager.setAPStaticIPConfig(IPAddress(10, 0, 1, 1), IPAddress(10, 0, 1, 1), IPAddress(255, 255, 255, 0));

  wifiManager.setAPCallback(configModeCallback);
  wifiManager.setSaveConfigCallback(saveConfigCallback);
  wifiManager.autoConnect("ShowerIO", "12345678");


  pinMode(buttonPin, INPUT);
  pinMode(rele, OUTPUT);
  pinMode(Led_Aviso, OUTPUT);

  digitalWrite(rele, LOW);
  digitalWrite(Led_Aviso, LOW);


  botao.attach(0.2, logica_botao);
  tyme.attach(1, logica_tempo);

  DBG_OUTPUT_PORT.begin(115200);
  DBG_OUTPUT_PORT.print("\n");
  DBG_OUTPUT_PORT.setDebugOutput(true);

  //Maps and scan all the SPIFFS data directory
  SPIFFS.begin();
  {
    Dir dir = SPIFFS.openDir("/");
    while (dir.next()) {
      String fileName = dir.fileName();
      size_t fileSize = dir.fileSize();
      DBG_OUTPUT_PORT.printf("FS File: %s, size: %s\n", fileName.c_str(), formatBytes(fileSize).c_str());
    }
    DBG_OUTPUT_PORT.printf("\n");
  }

  //  //---------- DNS Server
  //  dnsServer.setTTL(300);
  //  dnsServer.setErrorReplyCode(DNSReplyCode::ServerFailure);
  //  dnsServer.start(DNS_PORT, "www.smartshower.com", apip); // replace it with desired DNS name
  //  //-----------------

  DBG_OUTPUT_PORT.println("");
  DBG_OUTPUT_PORT.print("Connected! IP address: ");
  DBG_OUTPUT_PORT.println(WiFi.localIP());

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  if (mdns.begin("SmartShower", WiFi.localIP()))
    Serial.println("MDNS Responder Started!");


  //EEPROM Start Function
  EEPROM.begin(512);

  armazenado = EEPROM.read(address_tempo);
  if (armazenado > 45 || armazenado < 1 ) {
    minutos = 7;
    EEPROM.write(address_tempo, minutos);
    EEPROM.commit();
  }
  else {
    minutos = armazenado;
  }

  armazenado = EEPROM.read(address_espera);
  if (armazenado > 45 || armazenado < 1 ) {
    minutos_espera = 5;
    EEPROM.write(address_espera, minutos_espera);
    EEPROM.commit();
  }
  else {
    minutos_espera = armazenado;
  }

  armazenado = EEPROM.read(address_pausa);
  if (armazenado > 45 || armazenado < 1 ) {
    minutos_pausa = 3;
    EEPROM.write(address_pausa, minutos_pausa);
    EEPROM.commit();
  }
  else {
    minutos_pausa = armazenado;
  }

  server.on ( "/verifyAccountExistance", verifyAccountExistance);
  server.on ( "/verifyCredentials", verifyCredentials);
  server.on ( "/createCredentials", createCredentials);
  server.on ( "/check", check);
  server.on ( "/selectDurationTime", selectDurationTime);
  server.on ( "/setActualShowerTimePlus", setActualShowerTimePlus);
  server.on ( "/setActualShowerTimeLess", setActualShowerTimeLess);
  server.on ( "/selectOffTime", selectOffTime);
  server.on ( "/setActualOffTimePlus", setActualOffTimePlus);
  server.on ( "/setActualOffTimeLess", setActualOffTimeLess);
  server.on ( "/selectPausedTime", selectPausedTime);
  server.on ( "/setActualPausedTimePlus", setActualPausedTimePlus);
  server.on ( "/setActualPausedTimeLess", setActualPausedTimeLess);
  server.on ( "/setActualPausedTimeLess", setActualPausedTimeLess);
  server.on ( "/getDurationTime", getDurationTime);
  server.on ( "/getOffTime", getOffTime);
  server.on ( "/getPausedTime", getPausedTime);
  server.on ( "/reset", resetWifiManagerSettings);

  server.serveStatic("/", SPIFFS, "/", "max-age=86400");

  server.begin();

  DBG_OUTPUT_PORT.println("HTTP server started");

  MDNS.addService("http", "tcp", 80);

}



void loop(void) {
  server.handleClient();
}

