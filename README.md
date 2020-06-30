# embedded-system-project

This is a project I did with a classmate during my third year at my engineering school.
The goal was to control a small car with our phone.

*Electronic part (with the software Altium):
1) positionning the components on our printed circuit board (PCB);
2) PCB routing;
3) printing of the card.

*Programming part:
1) coding our card with SystemWorkbench for STM32;
2) coding an app to control the car with our phone.



To program our PCB, we used a state machine:

![state_machine](https://user-images.githubusercontent.com/46608797/86120365-76f7b600-bad4-11ea-97ca-afef716ad013.png)

The following activity diagram shows how the phone app works:

![activity_diagram_app](https://user-images.githubusercontent.com/46608797/86119811-73affa80-bad3-11ea-80ca-da7767e80786.png)

Here is how it looks, with the different characters sent to the PCB:

![app](https://user-images.githubusercontent.com/46608797/86120730-203eac00-bad5-11ea-8b96-374d101b38b5.png)










