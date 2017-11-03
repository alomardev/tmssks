<?php
require_once "../php/app.php";

$devices = array();
$kids = array();

$query =
"
SELECT DISTINCT `devices`.`id` as `id`, `devices`.`type` as `type`,
	CASE `devices`.`type`
		WHEN 1 THEN `trans`.`num_plate`
		ELSE `schools`.`name`
	END as `holder`
FROM `devices`, `schools`, `trans`
WHERE `devices`.`type` = 2 AND `schools`.`device_id`=`devices`.`id`
	OR `devices`.`type` = 1 AND `trans`.`device_id`=`devices`.`id`
ORDER BY `id`;
";

$result = mysqli_query($link, $query);

if ($result) {
	$devices = mysqli_fetch_all($result, MYSQLI_ASSOC);
}

$result = mysqli_query($link, "SELECT `nid`, `name` FROM `kids`;");

if ($result) {
	$kids = mysqli_fetch_all($result, MYSQLI_ASSOC);
}

header("Content-Type: application/json");
echo json_encode(array("devices" => $devices, "kids" => $kids));
?>
