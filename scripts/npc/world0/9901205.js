// NX Exchanger

importPackage(Packages.client);

var status = 0;

status = -1;

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
        var npcmessage = "Hi there! I can exchange your NX Credit to NX Prepaid. This is so you can gift someone in the cash shop. I can also exchange your left over NX Prepaid back to NX Credit. What would you like to do?\r\n\r\n";
        cm.sendSimple(npcmessage + "#b#e#L0#Exchange NX Credit to NX Prepaid#l\r\n#L1#Exchange NX Prepaid to NX Credit#l#k#n");
    }
    else if (status == 1)
    {
        if(selection == 0)
        {
            var nxcredit = cm.getPlayer().getCashShop().getCash(1);
            cm.sendGetNumber("How much NX Credit would you like to exchange?\r\n\r\n#bYou currently have " + nxcredit + " NX Credit.#k", 1, 1, nxcredit);
        }
        else if (selection == 1)
        {
            var nxprepaid = cm.getPlayer().getCashShop().getCash(4);
            status = 2;
            cm.sendGetNumber("How much NX Prepaid would you like to exchange?\r\n\r\n#bYou currently have " + nxprepaid + " NX Prepaid.#k", 1, 1, nxprepaid);
        }
    }
    else if (status == 2)
    {
        var exchange = selection;

        if(cm.getPlayer().getCashShop().getCash(1) >= exchange)
        {
            cm.getPlayer().getCashShop().gainCash(1, -exchange);
            cm.getPlayer().getCashShop().gainCash(4, exchange);

            cm.sendOk("You have exchanged " + exchange + " NX Credit to NX Prepaid.");
            cm.dispose();
        }
    }
    else if (status == 3)
    {
        var exchange = selection;

        if(cm.getPlayer().getCashShop().getCash(4) >= exchange)
        {
            cm.getPlayer().getCashShop().gainCash(4, -exchange);
            cm.getPlayer().getCashShop().gainCash(1, exchange);

            cm.sendOk("You have exchanged " + exchange + " NX Prepaid to NX Credit.");
            cm.dispose();
        }
    }
}