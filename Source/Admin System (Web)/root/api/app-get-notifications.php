<?php
header("Content-Type: Application/JSON");
require_once "../php/app.php";
$user_id = isset($_POST["user_id"]) ? $_POST["user_id"] : $_GET["user_id"];
$alert_timeout = isset($_POST["alert_timeout"]) ? ((int) $_POST["alert_timeout"]) : 1000 * 3;
$done_alerts = isset($_POST["done_alerts"]) && !empty($_POST["done_alerts"]) ? explode("|", $_POST["done_alerts"]) : array();
$done_notify = isset($_POST["done_notify"]) && !empty($_POST["done_notify"]) ? explode("|", $_POST["done_notify"]) : array();
$since = isset($_POST["since"]) ? $_POST["since"] : 0;
$nots = array();

$records = generateTimelineRecords($user_id);
$triggers = array();
foreach ($records as $k => $v) {
	foreach ($v as $r) {
		if ($r["type"] == 1) {
			$triggers[$k] = $r;
		}
		if (!in_array($r["reading_id"], $done_notify)) {
			$nots[] = array("nid" => $k, "type" => 2, "record" => $r);
		}
	}
}

foreach ($triggers as $k => $tr) {
	if ($tr["entered"] && time() >= $tr["time"] + $alert_timeout && !in_array($tr["reading_id"], $done_alerts)) {
			$nots[] = array("nid" => $k, "type" => $tr["type"], "record" => $tr);
	}
}

echo json_encode($nots);
?>
