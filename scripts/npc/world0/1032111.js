/* Author: aaroncsn(MapleSea Like)
	NPC Name: 		Small Trunk
	Map(s): 		Victoria Road : Top of the Tree That Grew (101010103)
	Description: 		A tree
*/

var status;

function start() 
{
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection)
{
	if (mode == 1)
	{
		status++;
	}
	else
	{
		cm.dispose();
		return;
	}

	if (status == 0)
	{
		if (cm.isQuestStarted(20716) && !cm.hasItem(4032142))
		{
			cm.gainItem(4032142);
			cm.sendOk("You have received a #v4032142# #t4032142#");
			cm.dispose();
		}
		else
		{
			cm.sendOk("A sweet scent of tree bark tickles my nose.");
			cm.dispose();
		}
	}
}