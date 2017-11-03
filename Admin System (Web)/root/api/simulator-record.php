<?php
require_once "../php/app.php";
$session_id = $_POST['session_id'];
$latitude = $_POST['latitude'];
$longitude = $_POST['longitude'];

echo uploadRecord($session_id, $latitude, $longitude);
?>
