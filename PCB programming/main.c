/**
  ******************************************************************************
  * @file    main.c
  * @author  Nirgal
  * @date    03-July-2019
  * @brief   Default main function.
  ******************************************************************************
*/
#include "stm32f1xx_hal.h"
#include "stm32f1_uart.h"
#include "stm32f1_sys.h"
#include "stm32f1_gpio.h"
#include "macro_types.h"
#include "systick.h"
#include "moteur.h"
#include "stm32f1_timer.h"

void condition1(void);
void condition2(void);

void writeLED(bool_e b)
{
	HAL_GPIO_WritePin(LED_GREEN_GPIO, LED_GREEN_PIN, b);
}

bool_e readButton(void)
{
	return !HAL_GPIO_ReadPin(BLUE_BUTTON_GPIO, BLUE_BUTTON_PIN);
}

static volatile uint32_t t = 0;

void process_ms(void)
{
	if(t){
		t--;
	}
}

uint8_t c = 0;
//static volatile uint16_t distance;

static uint16_t distance_main;

typedef enum{
	INIT = 0,
	AVANCE,
	DROITE,
	RECULE,
	GAUCHE,
	ARRET,
	ATTENTE_ORDRE,
	AUTO,
	AVANCE_AUTO,
	GAUCHE_AUTO,
	FIN_AUTO
}state_e;

static state_e state = INIT;

void state_machine(uint8_t c)
{
		switch(state){
			case INIT:
				state = ARRET ;
				break;
			case ATTENTE_ORDRE:
				if(UART_data_ready(UART2_ID))
				{
					c = UART_get_next_byte(UART2_ID);
					switch(c) //On commande les moteurs en fonction du caractère reçu
					{
						case 'a':
							state = AVANCE ;
						break;
						case 'b':
							state = DROITE ;
						break;
						case 'c':
							state = RECULE ;
						break;
						case 'd':
							state = GAUCHE ;
						break;
						case 'e':
							state  = ARRET ;
						break;
						case 'f':
							state = AUTO;
						break;
						case 'g':
							state = FIN_AUTO;
						break;
					}
				}
			break;

			case AVANCE:
				MOTEUR_avance();
				state = ATTENTE_ORDRE;
			break;

			case DROITE:
				MOTEUR_droite();
				state = ATTENTE_ORDRE;
				break;

			case RECULE:
				MOTEUR_recule();
				state = ATTENTE_ORDRE;
				break;

			case GAUCHE:
				MOTEUR_gauche();
				state = ATTENTE_ORDRE;
			break;

			case ARRET:
				MOTEUR_arret();
				state = ATTENTE_ORDRE;
				break;

			case AUTO:
				state = AVANCE_AUTO;
			break;

			case AVANCE_AUTO:
				MOTEUR_avance();
				condition1(); //obstacle
				condition2(); //retour mode manuel
			break;

			case GAUCHE_AUTO:
				MOTEUR_arret();
				HAL_Delay(1000);
				MOTEUR_gauche();
				HAL_Delay(500);
				MOTEUR_avance();
				HAL_Delay(500);

				state = AVANCE_AUTO;
			break;

			case FIN_AUTO:
				MOTEUR_arret();
				printf("Retour en mode manuel");
				state = ATTENTE_ORDRE;
			break;
		}
}





int main(void)
{
	//Initialisation de la couche logicielle HAL (Hardware Abstraction Layer)
	//Cette ligne doit rester la première étape de la fonction main().
	HAL_Init();

	//Initialisation de l'UART2 à la vitesse de 115200 bauds/secondes (92kbits/s) PA2 : Tx  | PA3 : Rx.
		//Attention, les pins PA2 et PA3 ne sont pas reliées jusqu'au connecteur de la Nucleo.
		//Ces broches sont redirigées vers la sonde de débogage, la liaison UART étant ensuite encapsulée sur l'USB vers le PC de développement.
	UART_init(UART2_ID,9600); //115200

	//"Indique que les printf sortent vers le périphérique UART2."
	SYS_set_std_usart(UART2_ID, UART2_ID, UART2_ID);

	//Initialisation du port de la led Verte (carte Nucleo)
	BSP_GPIO_PinCfg(LED_GREEN_GPIO, LED_GREEN_PIN, GPIO_MODE_OUTPUT_PP,GPIO_NOPULL,GPIO_SPEED_FREQ_HIGH);

	//Initialisation du port du bouton bleu (carte Nucleo)
	BSP_GPIO_PinCfg(BLUE_BUTTON_GPIO, BLUE_BUTTON_PIN, GPIO_MODE_INPUT,GPIO_PULLUP,GPIO_SPEED_FREQ_HIGH);

	//On ajoute la fonction process_ms à la liste des fonctions appelées automatiquement chaque ms par la routine d'interruption du périphérique SYSTICK
	Systick_add_callback_function(&process_ms);

	MOTEUR_init();

	while(1)	//boucle de tâche de fond
	{

		state_machine(c);


		if(!t)
		{
			t = 200;
		}

	}
}


void condition1(void){
	HCSR04_demo_state_machine(&distance_main);
	printf("distance %d\n", distance_main);
	if(distance_main<100 && distance_main != 0){
		state = GAUCHE_AUTO;
	}
}


void condition2(void){
	if(UART_data_ready(UART2_ID)){
		c = UART_get_next_byte(UART2_ID);
		if(c == 'g'){
			state = FIN_AUTO;
		}
	}
}


