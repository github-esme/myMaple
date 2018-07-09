function enter(pi) {
	if (pi.getPlayer().getParty() == null || !pi.isLeader()) {
		pi.message("The leader of the party must be here.");
		return false;
	} else {
		var party = pi.getPlayer().getParty().getMembers();
		var mapId = pi.getPlayer().getMapId();
		var next = true;
		var size = 0;
		var it = party.iterator();
		while (it.hasNext()) {
			var cPlayer = it.next();
			var ccPlayer = pi.getPlayer().getMap().getCharacterById(cPlayer.getId());
			if (ccPlayer == null) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 4 : 1);
		}	
		if (next && (pi.getPlayer().isGM() || size >= 1)) {
			for(var i = 0; i < 7; i++) {
				if (pi.getMap(pi.getMapId() + 1 + i) != null && pi.getMap(pi.getMapId() + 1 + i).getCharactersSize() == 0) {
					pi.warpParty(pi.getMapId() + 1 + i);
					pi.dispose();
					return true;
				}
			}
			pi.message("Another party quest has already entered this channel.");
			return false;
		} else {
			pi.message("At least one member of your party must be here.");
			return false;
		}
	}
}