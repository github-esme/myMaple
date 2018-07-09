/* Cygnus revamp
Quest Find the Crumpled Piece of Paper Again
Knocked Trash Can
Made by Daenerys
*/

var status = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection)
{
	if(mode == 1)
	{
		status++;
	}
	else
	{
		cm.dispose();
		return;
	}

	if(status == 0)
	{
		if (cm.isQuestStarted(2214) && !cm.haveItem(4031894)) { 
			cm.sendOk("You have found a Crumpled Piece of Paper.");
			cm.gainItem(4031894);
			cm.forceCompleteQuest();
			cm.dispose();
		} else {
			cm.sendOk("...");
			cm.dispose();
		}
	}
}