function start() {
	cm.getPlayer().getStorage().sendStorage(cm.getClient(), 1100000);
	//cm.sendOk("Storage has been disabled.");
	cm.dispose();
}