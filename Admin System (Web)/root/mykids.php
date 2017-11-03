<?php
require_once "php/app.php";
openSession("./login.php");
$query = "SELECT * FROM `parents` WHERE `user_id`=$session[id];";
$result = mysqli_query($link, $query);
$kids = array();
if ($result) {
	$nids = array();
	while ($row = mysqli_fetch_assoc($result)) {
		$nids[] = $row["kid_id"];
	}
	if (count($nids) > 0) {
		$query = "SELECT `kids`.`nid` as nid, `kids`.`level` as level, `kids`.`name` as kid_name, `schools`.`name` as school_name FROM `kids` LEFT JOIN `schools` ON `schools`.`id` = `kids`.`school_id` WHERE ";
		for ($i = 0; $i < count($nids); $i++) {
			if ($i > 0) {
				$query .= " OR ";
			}
			$nid = $nids[$i];
			$query .= "`kids`.`nid`='$nid'";
		}
		$query .= ";";
	}
	$result = mysqli_query($link, $query);
	if ($result) {
		$kids = mysqli_fetch_all($result, MYSQLI_ASSOC);
	}
}
include "php/ui.top.php"; ?>

<table class="data-table">
	<thead><tr align="left"><th>Name</th><th>National ID</th><th>School</th><th>Level</th></thead>
	<tbody>
	<?php
	foreach ($kids as $k => $v) {
		echo "<tr><td>$v[kid_name]</td><td>$v[nid]</td><td>$v[school_name]</td><td>$v[level]</td></tr>";
	}
	?>
	</tbody>
</table>
<?php include "php/ui.bottom.php"; ?>
