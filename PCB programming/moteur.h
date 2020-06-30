/*
 * fan.h
 *
 *  Created on: 26 juin 2019
 *      Author: Nirgal
 */

#ifndef MOTEUR_H_
#define MOTEUR_H_
#include "macro_types.h"

#define MOTEUR_GPIO				GPIOA
#define MOTEUR_AIN1				GPIO_PIN_9
#define MOTEUR_AIN2				GPIO_PIN_8
#define MOTEUR_BIN1				GPIO_PIN_10
#define MOTEUR_BIN2				GPIO_PIN_11

void MOTEUR_init(void);

void MOTEUR_avance(void);
void MOTEUR_recule(void);
void MOTEUR_droite(void);
void MOTEUR_gauche(void);
void MOTEUR_arret(void);


#endif /* MOTEUR_H_ */
