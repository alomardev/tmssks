<?php
header("Content-Type: Application/JSON");

require_once "../php/app.php";
$kid_id = isset($_POST["kid_id"]) ? $_POST["kid_id"] : $_GET["kid_id"];

$session_id = -1;
$result = mysqli_query($link, "SELECT `session_id` FROM `sessionKids` WHERE `kid_Id`='$kid_id'");
if ($result) {
	if ($row = mysqli_fetch_assoc($result)) {
		$session_id = $row['session_id'];
	}
}

$records = array();
$result = mysqli_query($link, "SELECT UNIX_TIMESTAMP(`time`) AS `time`, `latitude`, `longitude` FROM `records` WHERE `session_id`=$session_id ORDER BY `time` DESC LIMIT 1;");
if ($result) {
	$records = mysqli_fetch_assoc($result);
}

echo json_encode($records);
?>
