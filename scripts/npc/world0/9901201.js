// Rock-Paper-Scissors NPC

var status; 
var mesosBet;
var currentTimes = 1.5;
var amountWon;

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
    else if (mode == 0)
    {
        status--;
    }
    else
    {
        cm.dispose();
        return;
    }

    if (status == 0) 
    {
        cm.sendYesNo("I am a homeless guy on the street and is in need of quick cash. I'll tell you what. I'll play rock-paper-scissors with you. If you win, you can either cash out for 1.5x times the amount you bet or you can continue playing for a higher winning. Would you like to play?"); 
    } 
    else if (status == 1) 
    { 
        status = 8;
        var mesos = cm.getPlayer().getMeso();
        
        if(mesos < 1)
        {
            cm.sendOk("You don't have any mesos to bet.");
            cm.dispose();
        }
        else
        {
            cm.sendGetNumber("How much mesos you want to bet on? The max amount you can bet on is 10,000 to prevent too high of a jackpot.\r\n\r\n#bYou currently have " + mesos + " mesos.#k", 1, 1, 10000);
        }
    }
    else if (status == 9) // Start playing solo
    {
        mesosBet = selection;
        status = 10;
        cm.sendSimple("Choose your hand below. \r\n#L0#Rock#l\r\n#L1#Paper#l\r\n#L2#Scissors#l");
    }
    else if (status == 10) // Don't continue playing solo
    {
        cm.gainMeso(amountWon);
        cm.sendOk("Aw man, it was just getting fun. As promised, here's your mesos. #eYou won " + amountWon + " mesos.#n");
        cm.dispose();
    }
    else if (status == 11)
    {
        var choseArray = new Array("Rock", "Paper", "Scissors");
        var playerChose = choseArray[selection];
        var gameChose = choseArray[Math.floor(Math.random() * 3)];

        if (playerChose == gameChose)
        {
            if (amountWon == null)
            {
                cm.sendOk("It was a draw!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("It was a draw! You have won or lost nothing.");
                cm.dispose();
            }
        }
        else
        {
            if (playerChose == "Rock" && gameChose == "Paper")
            {
                cm.gainMeso(-mesosBet);
                cm.sendOk("You have lost! My hand was a " + gameChose + ". Thanks for " + mesosBet + " mesos.");
                cm.dispose();
            }
            else if (playerChose == "Paper" && gameChose == "Scissors")
            {
                cm.gainMeso(-mesosBet);
                cm.sendOk("You have lost! My hand was a " + gameChose + ". Thanks for " + mesosBet + " mesos.");
                cm.dispose();
            }
            else if (playerChose == "Scissors" && gameChose == "Rock")
            {
                cm.gainMeso(-mesosBet);
                cm.sendOk("You have lost! My hand was a " + gameChose + ". Thanks for " + mesosBet + " mesos.");
                cm.dispose();
            }
            else
            {
                amountWon = mesosBet * currentTimes;
                var timesWon = currentTimes / 0.5 - 2;

                if (timesWon == 3) 
                {
                    Packages.net.server.Server.getInstance().broadcastMessage(Packages.tools.MaplePacketCreator.serverNotice(6, "[Rock-Paper-Scissors] " + cm.getPlayer().getName() + " has won 3 times in a row on Rock-Paper-Scissors!"));
                }
                else if (timesWon == 5)
                {
                    Packages.net.server.Server.getInstance().broadcastMessage(Packages.tools.MaplePacketCreator.serverNotice(6, "[Rock-Paper-Scissors] " + cm.getPlayer().getName() + " has won 5 times in a row on Rock-Paper-Scissors!"));
                }
                else if (timesWon > 5)
                {
                    Packages.net.server.Server.getInstance().broadcastMessage(Packages.tools.MaplePacketCreator.serverNotice(6, "[Rock-Paper-Scissors] " + cm.getPlayer().getName() + " has won " + timesWon + " times in a row on Rock-Paper-Scissors!"));
                }

                cm.sendYesNo("You have won! My hand was a " + gameChose + ". Would you like to continue playing to win #b#e" + (currentTimes + 0.5) + "x#k#n the amount you bet?\r\n\r\n#eCurrent jackpot: " + amountWon + "\r\nNext jackpot: " + mesosBet * (currentTimes + 0.5) + "#n");
                
                currentTimes = currentTimes + 0.5;
            }
        }
    }
    else if (status == 12) // Continue playing solo
    {
        status = 10;
        cm.sendSimple("Choose one: \r\n#L0#Rock#l\r\n#L1#Paper#l\r\n#L2#Scissors#l");
    }
    else
    {
        cm.dispose();
    }
}  