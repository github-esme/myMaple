// Gaga - Housing

var housing = new Packages.server.events.Housing;
var allHouses;
var charsInMap;
var position = 0;

function start() 
{
    status = -1;

    allHouses = housing.getAllHousingMaps();

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
        var isInHouse = false;

        for (var i = 0; i < allHouses.size(); i++)
        {
            if (allHouses.get(i) == cm.getPlayer().getMapId())
            {
                isInHouse = true;
            }
        }

        if (isInHouse)
        {
            if (housing.isOwner(cm.getPlayer().getMapId(), cm.getPlayer().getName()))
            {
                var outStr = "What would you like to do?\r\n\r\n";

                outStr += "#e#b#L0#Kick a player out of the house#l\r\n";
                outStr += "#L1#Change the house password#l\r\n";
                outStr += "#L2#Change the house name#l\r\n";
                outStr += "#L3#Exit the house#l#k#n\r\n";

                cm.sendSimple(outStr);
            }
            else if (housing.getOwner(cm.getPlayer().getMapId()) == "")
            {
                cm.warp(910000000);
                cm.dispose();
            }
            else
            {
                cm.sendYesNo("Would you like to leave this house?");
                status = 4;
            }
        }
        else
        {
            cm.sendOk("Hi there! I'm the housing manager NPC for the housing system in myMaple. You can not use me since you are not in a house!");
            cm.dispose();
        }
    } 
    else if (status == 1) // Kick player
    { 
        if (selection == 0)
        {
            var outStr = "Which player would you like to kick?\r\n\r\n#e";
            charsInMap = cm.getPlayer().getMap().getCharacters();

            var iter = charsInMap.iterator();
            while (iter.hasNext())
            {
                var chr = iter.next();

                if (!chr.isIntern() && chr != cm.getPlayer())
                {
                    outStr += "#L" + position + "#" + chr.getName() + "#l\r\n";
                }

                position++;
            }

            cm.sendSimple(outStr);
        }
        else if (selection == 1)
        {
            status = 2;
            cm.sendGetText("What would you like to set your house password as?\r\n#eMax Characters: 50#n");
        }
        else if (selection == 2)
        {
            status = 3;
            cm.sendGetText("What would your house name to be?\r\n#eMax Characters: 50#n");
        }
        else if (selection == 3)
        {
            cm.warp(910000000);
            cm.dispose();
        }
    }
    else if (status == 2)
    {
        var iter = charsInMap.iterator();
        var chr;
        var tempChr;
        var i = 0;

        while (iter.hasNext())
        {
            tempChr = iter.next();

            if (i == position - 1)
            {
                chr = tempChr;
            }

            i++;
        }

        if (cm.getPlayer().getMapId() == chr.getMapId())
        {
            chr.message("You have been kicked out of the house!");
            chr.changeMap(910000000);

            cm.sendOk(chr.getName() + " has been kicked out of the house.");
            cm.dispose();
        }
        else
        {
            cm.sendOk(chr.getName() + " is not in your house!");
            cm.dispose();
        }
    }
    else if (status == 3) // Change password
    {
        password = cm.getText();

        if (password.length > 50)
        {
            cm.sendOk("Please enter a password less than 50 characters!");
            cm.dispose();
        }
        else if (password == "")
        {
            cm.sendOk("Please enter a password!");
            cm.dispose();
        }
        else
        {
            housing.setPassword(cm.getPlayer().getMapId(), password);
            cm.sendOk("The house password has been changed to: " + password);
            cm.dispose();
        }
    }
    else if (status == 4) // Change house name
    {
        var houseName = cm.getText();

        if (houseName.length > 50)
        {
            cm.sendOk("Please enter a house name less than 50 characters!");
            cm.dispose();
        }
        else if (houseName == "")
        {
            cm.sendOk("Please enter a house name!");
            cm.dispose();
        }
        else
        {
            housing.setHouseName(cm.getPlayer().getMapId(), houseName);
            cm.sendOk("The house name has been changed to: " + houseName);
            cm.dispose();
        }
    }
    else if (status == 5) // Leave house
    {
        cm.warp(910000000);
        cm.dispose();
    }
    else
    {
        cm.dispose();
    }
}  