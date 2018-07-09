// Currency Exchanger

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
        cm.sendSimple("I'm the currency exchanger for myMaple! What would you like to do?\r\n\r\n#L0#Exchange 2 billion mesos to 1 #v4000313# #t4000313#k#n##l\r\n#L1#Exchange 1 #v4000313# #t4000313# to 2 billion mesos#l");
    }
    else if (status == 1)
    {
        if(selection == 0)
        {
            if(cm.getMeso() >= 2000000000000)
            {
                if(cm.canHold(4000313))
                {
                    cm.gainItem(4000313);
                    cm.gainMeso(-2000000000000);
                    cm.sendOk("Exchange successful!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Please make room in your ETC inventory!");
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("You do not have 2 billion mesos!");
                cm.dispose();
            }
        }
        else if (selection == 1)
        {
            if((cm.getMeso() + 2000000000000) > 2147483647)
            {
                cm.sendOk("You can not hold more than 2,147,483,647 mesos!");
                cm.dispose();
            }
            else
            {
                if(cm.hasItem(4000313))
                {
                    cm.gainMeso(2000000000000);
                    cm.gainItem(4000313, -1);
                    cm.sendOk("Exchange successful!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("You do not have a #v4000313# #t4000313# to exchange!");
                    cm.dispose();
                }
            }
        }
    }
}