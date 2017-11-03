<?php
require_once "../php/app.php";

$session_id = $_POST["session_id"];
$device_id = $_POST["device_id"];
$kid_ids = explode("|", $_POST["kid_ids"]);
echo readKid($kid_ids, $session_id, $device_id);

?>
