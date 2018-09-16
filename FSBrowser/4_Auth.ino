//AUTHENTICATION SECTION


void testCredentials() {

  File f = SPIFFS.open("/email.txt", "r");
  if (!f) {
    DBG_OUTPUT_PORT.println("file open failed");
  }
  String e = f.readStringUntil('\n');
  DBG_OUTPUT_PORT.println(e);


  File z = SPIFFS.open("/pass.txt", "r");
  if (!z) {
    DBG_OUTPUT_PORT.println("file open failed");
  }

  String p = z.readStringUntil('\n');
  DBG_OUTPUT_PORT.println(p);


}

String checkName() {
  String savedName;
  DBG_OUTPUT_PORT.println("Getting device name");
  File f = SPIFFS.open("/name.txt", "r++");
  if (!f) {
    DBG_OUTPUT_PORT.println("file open failed");
  }
  savedName = f.readStringUntil('\n');
  DBG_OUTPUT_PORT.println("Device name: " + savedName);
  return savedName;
}

boolean createName(String showerName) {
  boolean processFlag = true;
  DBG_OUTPUT_PORT.println("Cleaning last name");

  SPIFFS.remove("/name.txt");

  File f = SPIFFS.open("/name.txt", "w+");
  if (!f) {
    processFlag = false;
    DBG_OUTPUT_PORT.println("file open failed");
  }
  DBG_OUTPUT_PORT.println("Saving name");
  f.print(showerName);

  return processFlag;

}

boolean deleteName(){
  boolean processFlag = true;
  DBG_OUTPUT_PORT.println("Cleaning last name");
  SPIFFS.remove("/name.txt");
  File f = SPIFFS.open("/name.txt", "w+");
   if (!f) {
    processFlag = false;
    DBG_OUTPUT_PORT.println("file open failed");
  }
  return processFlag;
}


boolean saveCredentials(String email, String password) {

  boolean processFlag = true;
  DBG_OUTPUT_PORT.println("Cleaning last credentials");
  SPIFFS.remove("/email.txt");
  SPIFFS.remove("/pass.txt");

  File f = SPIFFS.open("/email.txt", "w+");
  if (!f) {
    processFlag = false;
    DBG_OUTPUT_PORT.println("file open failed");
  }
  DBG_OUTPUT_PORT.println("Saving email");
  f.print(email);

  File z = SPIFFS.open("/pass.txt", "w+");
  if (!z) {
    processFlag = false;
    DBG_OUTPUT_PORT.println("file open failed");
  }
  DBG_OUTPUT_PORT.println("Saving password");
  z.print(password);
  f.close();
  z.close();
  testCredentials();

  return processFlag;

}


String checkCredentials(String email, String password) {
  String processResult = "VALIDATED";

  File f = SPIFFS.open("/email.txt", "r");
  if (!f) {
    processResult = "ERROR";
    DBG_OUTPUT_PORT.println("file open failed");
  }
  String e = f.readStringUntil('\n');
  DBG_OUTPUT_PORT.println(e);
  if (!email.equals(e)) {
    processResult = "EMAIL";
  }
  File z = SPIFFS.open("/pass.txt", "r");
  if (!z) {
    processResult = "ERROR";
    DBG_OUTPUT_PORT.println("file open failed");
  }
  String p = z.readStringUntil('\n');
  DBG_OUTPUT_PORT.println(p);
  if (!password.equals(p)) {
    processResult = "PASSWORD";
  }

  return processResult;
}

String checkExistance() {
  File z = SPIFFS.open("/pass.txt", "r");
  String existance;
  String p = "";
  p = z.readStringUntil('\n');
  if (p.equals("")) {
    existance = "N";
    DBG_OUTPUT_PORT.println("The credentials doesn't exists!");
  } else {
    existance = "Y";
    DBG_OUTPUT_PORT.println("The credentials exists!");
  }

  return existance;
}


void dropStoredCredentials() {
  DBG_OUTPUT_PORT.println("Cleaning last credentials");
  SPIFFS.remove("/email.txt");
  SPIFFS.remove("/pass.txt");

}




