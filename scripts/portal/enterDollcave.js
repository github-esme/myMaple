function enter(pi) {
    pi.removeNpc(910510000, 1104000);
    var map = pi.getMap(910510000);
    map.spawnNpc(1104000, new java.awt.Point(329, 257));
    pi.warp(910510000, 0);

    return true;
}