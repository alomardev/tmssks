<?php
require_once "../php/app.php";
$user_id = isset($_POST["user_id"]) ? $_POST["user_id"] : $_GET["user_id"];

header("Content-Type: Application/JSON");
echo json_encode(generateTimelineRecords($user_id));

?>
