var minLevel = 40;
var maxLevel = 255;
var minPlayers = 5;

var status = -1;

var APQItems = new Array(4031595, 4031597, 4031594);

var invalidPlayers = new Array();

var pqCheck = new Packages.server.partyquest.PQLimit;

function start() 
{
    action(1, 0, 0);
}

function action(mode, type, selection)
{
    if (mode != 1)
    {
        cm.dispose();
        return;
    }
    else
    {
        status++;
    }

    if (status == 0)
    {
        cm.sendSimple("What would you like to do?\r\n\r\n#L0#I want to leave this place#l\r\n#L1#I want to do the APQ#l");
    }
    else if (status == 1)
    {
        if (selection == 0)
        {
            cm.warp(670010000);
            cm.dispose();
        }
        else if (selection == 1)
        {
            if (!pqCheck.canPQ(cm.getPlayer(), "AmoriaPQ"))
            {
                var waitTime = pqCheck.getWaitTime(cm.getPlayer(), "AmoriaPQ");

                cm.sendOk("You have reached your daily limit of Amoria PQ! You can only do this PQ twice per 24 hours per account. Your APQ limit will reset after #e" + waitTime + ".#n");

                cm.dispose();
                return;
            }

            if (cm.getParty() == null) 
            {
                cm.sendOk("Please come back to me after you've formed a party.");
                cm.dispose();
                return;
            }
            if (!cm.isLeader()) 
            {
                cm.sendOk("You are not the party leader.");
                cm.dispose();
            } 
            else 
            {
                var party = cm.getParty().getMembers();
                var next = true;
                var levelValid = 0;
                var inMap = 0;

                if (party.size() < minPlayers)
                {
                    next = false;
                }
                else 
                {
                    for (var i = 0; i < party.size() && next; i++) 
                    {
                        var chr = party.get(i);

                        if ((chr.getLevel() >= minLevel) && (chr.getLevel() <= maxLevel))
                        {
                            levelValid++;
                        }

                        if (chr.getMapId() == cm.getPlayer().getMapId())
                        {
                            inMap++;
                        }

                        if (!pqCheck.canPQ(chr.getPlayer(), "AmoriaPQ"))
                        {
                            invalidPlayers.push(chr.getName());
                        }
                    }

                    if (levelValid < minPlayers || inMap < minPlayers)
                    {
                        next = false;
                    }

                }
                if (!next) 
                {
                    cm.sendOk("Your party is not a party of six. Make sure all your members are present and qualified to participate in this quest. I see #b" + levelValid.toString() + " #kmembers are in the right level range, and #b" + inMap.toString() + "#k are in my map. If this seems wrong, #blog out and log back in,#k or reform the party.");
                    cm.dispose();
                }
                else if (invalidPlayers.length != 0)
                {
                    var outStr = "These players below have already reached their daily Amoria PQ limit:\r\n\r\n";

                    for (var i = 0; i < invalidPlayers.length; i++)
                    {
                        outStr += "#e" + invalidPlayers[i] + "#n\r\n";
                    }

                    cm.sendOk(outStr);
                    cm.dispose();
                }
                else 
                {
                    for (var i = 0; i < APQItems.length; i++)
                    {
                        cm.removePartyItems(APQItems[i]);
                    }

                    var em = cm.getEventManager("AmoriaPQ");
                    if (em == null)
                    {
                        cm.dispose();
                    }
                    else
                    {
                        em.startInstance(cm.getParty(),cm.getPlayer().getMap());
                    }

                    cm.dispose();
                }
            }
        }
    }
}