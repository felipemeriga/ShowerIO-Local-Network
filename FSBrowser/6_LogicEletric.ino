//LogicEletric FUNCTIONS .INO FILE 

void logica_botao() {

  int reading = digitalRead(buttonPin);

  if (reading != lastButtonState) {
    // reset the debouncing timer
    lastDebounceTime = millis();
  }

  if ((millis() - lastDebounceTime) > debounceDelay) {

    if (reading != buttonState) {
      buttonState = reading;

      // only toggle the LED if the new button state is HIGH
      if (buttonState == LOW) {
        if (Banho == habilitado && Estado_Bot == desligado ) {
          Estado_Bot = iniciar_banho;
          digitalWrite(rele, HIGH);

        }

        else if (Banho == habilitado && (Estado_Bot == iniciar_banho || Estado_Bot == continuar_banho) ) {
          Estado_Bot = pausar_banho;
          minutos_pausa = EEPROM.read(address_pausa);
          tempo_de_pausa = minutos_pausa * 60;
          digitalWrite(rele, LOW);
        }

        else if (Banho == habilitado && Estado_Bot == pausar_banho ) {
          Estado_Bot = continuar_banho;
          digitalWrite(rele, HIGH);
        }
      }

    }
  }

  lastButtonState = reading;
}


void logica_tempo() {
  if (Banho == habilitado && Estado_Bot == iniciar_banho) {
    minutos = EEPROM.read(address_tempo);
    tempo = minutos * 60;
    Estado_Bot = continuar_banho;
  }

  else if (Banho == habilitado && Estado_Bot == continuar_banho) {
    tempo = tempo - 1;
    if (tempo < 60 && tempo > 20)
    {
      led_S = HIGH;
    }
    else if (tempo < 20) {

      if (led_S == LOW) {
        led_S = HIGH;
      } else {
        led_S = LOW;
      }


    }
    digitalWrite(Led_Aviso, led_S);
  }

  else if (Banho == habilitado && Estado_Bot == pausar_banho && tempo_de_pausa > 0) {
    tempo = tempo;
    tempo_de_pausa = tempo_de_pausa - 1;
  }
  else if (Banho == habilitado && Estado_Bot == pausar_banho && tempo_de_pausa <= 0) {
    Banho = desabilitado;
    Estado_Bot = desligado;
    tempo = 0;
    minutos_espera = EEPROM.read(address_espera);
    tempo_espera = (int)minutos_espera * 60;
    digitalWrite(rele, LOW);
  }

  if (tempo == 0 && Banho == habilitado) {
    Banho = desabilitado;
    Estado_Bot = desligado;
    minutos_espera = EEPROM.read(address_espera);
    tempo_espera = (int)minutos_espera * 60;
    digitalWrite(rele, LOW);
    digitalWrite(Led_Aviso, LOW);
  }

  else if (tempo == 0 && Banho == desabilitado) {
    if (tempo_espera > 0) {
      tempo_espera = tempo_espera - 1;
    }
    else if (tempo_espera <= 0) {
      Banho = habilitado;
      minutos = EEPROM.read(address_tempo);
      tempo = (int)minutos * 60;
      minutos_espera = EEPROM.read(address_espera);
      tempo_espera = (int)minutos_espera * 60;
    }
  }

}

