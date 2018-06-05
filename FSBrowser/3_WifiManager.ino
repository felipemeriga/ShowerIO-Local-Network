//WIFIMANAGER CALLBACKS .INO FILE

//Callback that when the ESP enters on AP
void configModeCallback (WiFiManager *myWiFiManager) {

  Serial.println("Entered on configuration mode");
  Serial.println(WiFi.softAPIP()); //prints the IP address
  Serial.println(myWiFiManager->getConfigPortalSSID()); //prints the created SSID of the network

}

//Callback when is connected to a new network and saved it successfully
void saveConfigCallback () {

  Serial.println("Configuration Saved!");
  Serial.println(WiFi.softAPIP()); //prints the IP address

  MDNS.notifyAPChange();
  MDNS.update();
}
