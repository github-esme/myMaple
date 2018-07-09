// Rooney - Guild JQ NPC

var status = 0;

var JQRankings = new Packages.server.events.JQRankings;

function start() 
{
	status = -1;
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
		cm.getPlayer().stopGuildJQTime();

		var eim = cm.getPlayer().getEventInstance();

		if (eim != null)
        {
            var basePoint = parseInt(eim.getProperty("basePoints"));
            var defaultBonus = parseInt(eim.getProperty("bonusPoints"));

        	if (cm.getGuild() == null)
        	{
        	    cm.sendOk("You must be in a guild!");
        	    cm.dispose();
        	    return;
        	}

            var fastest = JQRankings.getFastestTime(cm.getPlayer().getMapId());

            if (fastest == -1 || fastest == 0)
            {
                fastest = 10000;
            }

            if (cm.getPlayer().getGuildJQTime() < fastest)
            {
                JQRankings.updateRanking(cm.getPlayer().getName(), cm.getPlayer().getMapId(), cm.getPlayer().getGuildJQTime());
                Packages.net.server.Server.getInstance().broadcastMessage(Packages.tools.MaplePacketCreator.serverNotice(6, "[Jump Quest] " + cm.getPlayer().getName() + " has gotten the fastest time of " + cm.getPlayer().getGuildJQTime() + " seconds in the jump quest map " + cm.getPlayer().getMap().getMapName() + "."));
            }
            else if (JQRankings.getTime(cm.getPlayer().getName(), cm.getPlayer().getMapId()) < cm.getPlayer().getGuildJQTime())
            {
                JQRankings.updateRanking(cm.getPlayer().getName(), cm.getPlayer().getMapId(), cm.getPlayer().getGuildJQTime());
                cm.getPlayer().message("You have beat your past record in this Jump Quest! Your new record is now " + cm.getPlayer().getGuildJQTime() + " seconds.");
            }

        	if (eim.getPlayerCount() == 1)
            {
            	cm.logGuildJQ(cm.getPlayer().getGuildJQTime());
            	var bonus = parseInt(eim.getProperty("bonus"));

            	cm.getGuild().gainGP(basePoint + bonus);

            	eim.setProperty("bonus", bonus.toString());

            	cm.getPlayer().message("Your guild has earned " + (basePoint + bonus) + " guild points for your completion of this jump quest! You have completed this JQ in " + cm.getPlayer().getGuildJQTime() + " seconds.");

            	eim.unregisterPlayer(cm.getPlayer());
            	eim.dispose();

                cm.getPlayer().changeMap(910000000);

                cm.dispose();
            }
            else
            {
            	cm.logGuildJQ(cm.getPlayer().getGuildJQTime());

            	var bonus = parseInt(eim.getProperty("bonus"));

            	cm.getGuild().gainGP(basePoint + bonus);

            	cm.getPlayer().message("Your guild has earned " + (basePoint + bonus) + " guild points for your completion of this jump quest! You have completed this JQ in " + cm.getPlayer().getGuildJQTime() + " seconds.");

				bonus = bonus + defaultBonus;
            	eim.setProperty("bonus", bonus.toString());
                eim.removePlayer(cm.getPlayer()); // Triggers playerExit

                for(var i = 0; i < eim.getPlayerCount(); i++)
    			{
    			    var player = eim.getPlayers().get(i);
			
    			    player.dropMessage(5, cm.getPlayer().getName() + " has completed this JQ. The next player completing the JQ will earn " + (basePoint + bonus) + " guild points.");
    			}

                cm.dispose();
            }
        }
	}
}