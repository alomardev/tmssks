<?php
require_once "../php/app.php";
$user_id = isset($_POST["user_id"]) ? $_POST["user_id"] : -1;

$query = "SELECT `kids`.* FROM `parents` INNER JOIN `kids` ON `parents`.`kid_id`=`kids`.`nid` WHERE `parents`.`user_id`=$user_id;";
$result = mysqli_query($link, $query);
$kids = array();
$trans = array();
$schools = array();
if ($result) {
	$kids = mysqli_fetch_all($result, MYSQLI_ASSOC);
}
if (count($kids) > 0) {
	$tc = "";
	$first = true;
	foreach ($kids as $v) {
		if (empty($v['trans_id'])) {
			continue;
		}

		if (!$first) {
			$tc .= " OR";
		}
		$tc .= " `id`=$v[trans_id]";
		$first = false;
	}
	$query = "SELECT * FROM `trans` WHERE$tc;";
	$result = mysqli_query($link, $query);
	if ($result) {
		$trans = mysqli_fetch_all($result, MYSQLI_ASSOC);
	}

	$sc = "";
	$first = true;
	foreach ($kids as $v) {
		if (empty($v['school_id'])) {
			continue;
		}

		if (!$first) {
			$sc .= " OR";
		}
		$sc .= " `id`=$v[school_id]";
		$first = false;
	}
	$query = "SELECT * FROM `schools` WHERE$sc;";
	$result = mysqli_query($link, $query);
	if ($result) {
		$schools = mysqli_fetch_all($result, MYSQLI_ASSOC);
	}
}
$arr = array("trans" => $trans, "schools" => $schools, "kids" => $kids);

header("Content-Type: Application/JSON");
echo json_encode($arr);
?>
