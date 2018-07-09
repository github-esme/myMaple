var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Talk to Dr. De Lang.");
	qm.forceStartQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	qm.dispose();
}
