<?php

/* Global varables */
$link = null; // For database connection
$session = null; // For sessioning

/* Constants */
define("KEY", "xS0cIn5swsMxStgXoQeVs6r8p");
define("COOKIE_ID", "id");

$local = true;
define("HOST", $local ? "localhost" : "localhost");
define("USERNAME", $local ? "root" : "id1636587_alomartest");
define("PASSWORD", $local ? "" : "9229.Tmssks");
define("DB", $local ? "tmssks_db" : "id1636587_tmssks_db");

define("ROLE_ROOT_ADMIN", 0);
define("ROLE_SCHOOL_ADMIN", 1);
define("ROLE_PARENT", 2);

define("FEEDBACK_TYPE_NORMAL", 1);
define("FEEDBACK_TYPE_ERROR", 2);
define("FEEDBACK_TYPE_WARNING", 3);

/* Database Initialization */

$link = mysqli_connect(HOST, USERNAME, PASSWORD, DB);

if (!$link) {
    echo "Error: " . mysqli_connect_error();
    exit;
}

mysqli_set_charset($link, 'utf8'); // to support Arabic

/* Sessioning */
// Detect whether user is already logged in or not
function openSession($redirect = "") {
	global $session, $link; // to let the variables above used

	if (isset($_COOKIE[COOKIE_ID])) {
		$split = explode("|", $_COOKIE[COOKIE_ID]);
		if (md5($split[0].KEY) === $split[1]) { // check whether user is logged in or not
			$session = array();
			$session['id'] = $split[0];
			$session['username'] = "";
			$session['email'] = "";
			$session['name'] = "";
			$session['role'] = -1;

			$query = "SELECT `name`, `username`, `role`, `email` FROM `users` WHERE `id`=$session[id];";
			$result = mysqli_query($link, $query);
			if ($result && mysqli_num_rows($result) > 0) {
				$row = mysqli_fetch_assoc($result);
				$session['name'] = $row["name"];
				$session['username'] = $row["username"];
				$session['email'] = $row["email"];
				$session['role'] = $row["role"];
			}
			return true;
		} else {
			setcookie(COOKIE_ID, "", 1);
			if (!empty($redirect)) {
				header("location: $redirect");
				exit;
			}
			return false;
		}
	}
	if (!empty($redirect)) {
		header("location: $redirect");
		exit;
	}
	return false;
}

/* General */

function jsonFeedback($type, $message) {
	return json_encode(array('type'=>$type, 'message'=>$message));
}

/* Messages */

function getMessages() {
	global $session, $link;
	$list = array();
	$id = $session['id'];
	$query = "SELECT * FROM `messages` WHERE `to_id`=$id;";
	$result = mysqli_query($link, $query);
	if ($result) {
		$list = mysqli_fetch_all($result, MYSQLI_ASSOC);
	} else {
		echo mysqli_error($link);
	}

	return $list;
}

/* Users */

function getUsers($role) {
	global $link;
	$list = array();
	$query = "SELECT * FROM `users` WHERE `role`=$role;";
	$result = mysqli_query($link, $query);
	if ($result) {
		$i = 0;
		while ($row = mysqli_fetch_assoc($result)) {
			$phonesQuery = "SELECT `phone` FROM `phones` WHERE `user_id`=$row[id];";
			$phonesResult = mysqli_query($link, $phonesQuery);
			if ($phonesResult) {
				$phonesList = mysqli_fetch_all($phonesResult, MYSQLI_ASSOC);
				$j = 1;
				foreach ($phonesList as $v) {
					$row["phone".$j] = $v["phone"];
					$j++;
				}
			}
			$list[$i++] = $row;
		}
	}
	return $list;
}

function saveUser($user) {
	global $link;
	$processed_id;
	if (!empty($user['id'])) {
		$query = "UPDATE `users` SET `name`='$user[name]', `email`='$user[email]'";
		$query .= isset($user['password']) && !empty($user['password']) ? ", `password`='$user[password]'" : "";
		$query .= isset($user['nid']) && !empty($user['nid']) ? ", `nid`='$user[nid]'" : "";
		$query .= " WHERE `id`=$user[id];";
		$result = mysqli_query($link, $query);
		$processed_id = $user['id'];
		if (!$result) {
			return jsonFeedback(FEEDBACK_TYPE_ERROR, "Couldn't update the record! " . mysqli_error($link));
		}
		$query = "DELETE FROM `phones` WHERE `user_id`=$processed_id;";
		mysqli_query($link, $query);

		if (isset($user['old_password']) && !empty($user['old_password'])) {
			$query = "UPDATE `users` SET `password`='$user[password]' WHERE `id`=$user[id] AND BINARY `password`='$user[old_password]'";
			mysqli_query($link, $query);
		}

	} else {
		$query = "INSERT INTO `users` SET `name`='$user[name]', `username`='$user[username]', `password`='$user[password]', `email`='$user[email]', `nid`='$user[nid]', `role`=$user[role];";
		$result = mysqli_query($link, $query);
		if (!$result) {
			return jsonFeedback(FEEDBACK_TYPE_ERROR, "Couldn't insert the new record! " . mysqli_error($link));
		}

		$processed_id = mysqli_insert_id($link);
	}

	if (isset($user['phone1']) && !empty($user['phone1'])) {
		$query = "INSERT INTO `phones` VALUES ($processed_id, '$user[phone1]')";
		if (isset($user['phone2']) && !empty($user['phone2'])) {
			$query .= ", ($processed_id, '$user[phone2]')";
		}
		$query .= ";";
		$result = mysqli_query($link, $query);
		if (!$result) {
			return jsonFeedback(FEEDBACK_TYPE_WARNING, "Couldn't assign phone number(s)");
		}
	}
	return jsonFeedback(FEEDBACK_TYPE_NORMAL, "Done");
}

function deleteUser($id) {
	global $link;
	$query = "DELETE FROM `users` WHERE `id`=$id;";
	$result = mysqli_query($link, $query);
	if (!$result) {
		return jsonFeedback(FEEDBACK_TYPE_ERROR, "Couldn't delete the record!");
	}
	return jsonFeedback(FEEDBACK_TYPE_NORMAL, "Record deleted successfully.");
}

/* Trans */

function getTrans() {
	global $link;
	$list = array();
	$query = "SELECT * FROM `trans`";
	$result = mysqli_query($link, $query);
	if ($result) {
		$i = 0;
		while ($row = mysqli_fetch_assoc($result)) {
			$list[$i++] = $row;
		}
	}
	return $list;
}

function saveTrans($trans) {
	global $link;
	if (!empty($trans['id'])) {
		$query = "UPDATE `trans` SET `num_plate`='$trans[num_plate]', `driver_name`='$trans[driver_name]', `driver_phone`='$trans[driver_phone]' WHERE `id`=$trans[id];";
	} else {
		$query = "INSERT INTO `trans` SET `num_plate`='$trans[num_plate]', `driver_name`='$trans[driver_name]', `driver_phone`='$trans[driver_phone]';";
	}

	$result = mysqli_query($link, $query);
	if (!$result) {
		return jsonFeedback(FEEDBACK_TYPE_ERROR, "Error!" . " " . mysqli_error($link));;
	}
	return jsonFeedback(FEEDBACK_TYPE_NORMAL, "Done");
}

function deleteTrans($id) {
	global $link;
	$query = "DELETE FROM `trans` WHERE `id`=$id;";
	$result = mysqli_query($link, $query);
	if (!$result) {
		return jsonFeedback(FEEDBACK_TYPE_ERROR, "Couldn't delete the record!");
	}
	return jsonFeedback(FEEDBACK_TYPE_NORMAL, "Record deleted successfully.");
}

/* Kids */

function getKids() {
	global $link, $session;
	$school_id = -1;
	$schoolRes = mysqli_query($link, "SELECT `id` FROM `schools` WHERE `admin_id`=$session[id];");
	if ($schoolRes) {
		while ($row = mysqli_fetch_assoc($schoolRes)) {
			$school_id = $row['id'];
		}
	}

	$list = array();
	$query = "SELECT * FROM `kids` WHERE `school_id`=$school_id";
	$result = mysqli_query($link, $query);
	if ($result) {
		$i = 0;
		while ($row = mysqli_fetch_assoc($result)) {
			$pres = mysqli_query($link, "SELECT `user_id` FROM `parents` WHERE `kid_id`=$row[nid];");
			$pp = 0;
			while ($prow = mysqli_fetch_assoc($pres)) {
				$row['parent'.(++$pp)] = $prow['user_id'];
			}
			$list[$i++] = $row;
		}
	}
	return $list;
}

function saveKid($kid) {
	global $link, $session;

	$school_id = -1;
	$schoolRes = mysqli_query($link, "SELECT `id` FROM `schools` WHERE `admin_id`=$session[id];");
	if ($schoolRes) {
		while ($row = mysqli_fetch_assoc($schoolRes)) {
			$school_id = $row['id'];
		}
	}

	$result = mysqli_query($link, "SELECT * FROM `kids` WHERE `nid`=$kid[nid];");
	$exist = mysqli_num_rows($result) > 0;
	if ($exist) {
		$query = "UPDATE `kids` SET `name` = '$kid[name]'";
		$query .= (!empty($kid['level']) ? ", `level` = '$kid[level]'" : ", `level` = NULL");
		$query .= (!empty($kid['trans_id']) ? ", `trans_id` = '$kid[trans_id]'" : ", `trans_id` = NULL");
		$query .= (!empty($kid['address_title']) ? ", `address_title` = '$kid[address_title]'" : ", `address_title` = NULL");
		$query .= (!empty($kid['address_latitude']) ? ", `address_latitude` = '$kid[address_latitude]'" : ", `address_latitude` = NULL");
		$query .= (!empty($kid['address_longitude']) ? ", `address_longitude` = '$kid[address_longitude]'" : ", `address_longitude` = NULL");
		$query .= ($school_id > -1 ? ", `school_id` = $school_id" : ", `school_id` = NULL");
		$query .= " WHERE `nid`='$kid[nid]';";

		mysqli_query($link, "DELETE FROM `parents` WHERE `kid_id`='$kid[nid]';");
	} else {
		$key = md5(KEY.$kid['name'].KEY.$kid['nid'].KEY);
		$query = "INSERT INTO `kids` SET `nid` = '$kid[nid]', `name` = '$kid[name]', `key`='$key'";
		$query .= (!empty($kid['level']) ? ", `level` = '$kid[level]'" : "");
		$query .= (!empty($kid['trans_id']) ? ", `trans_id` = '$kid[trans_id]'" : "");
		$query .= (!empty($kid['address_title']) ? ", `address_title` = '$kid[address_title]'" : "");
		$query .= (!empty($kid['address_longitude']) ? ", `address_longitude` = '$kid[address_longitude]'" : "");
		$query .= (!empty($kid['address_latitude']) ? ", `address_latitude` = '$kid[address_latitude]'" : "");
		$query .= ($school_id > -1 ? ", `school_id` = $school_id" : "");
		$query .= ";";
	}

	$result = mysqli_query($link, $query);

	if (!$result) {
		return jsonFeedback(FEEDBACK_TYPE_ERROR, "Error! " . mysqli_error($link));;
	}

	if (!empty($kid['parent1'])) {
		mysqli_query($link, "INSERT INTO `parents` VALUES($kid[nid], $kid[parent1]);");
	}
	if (!empty($kid['parent2'])) {
		mysqli_query($link, "INSERT INTO `parents` VALUES($kid[nid], $kid[parent2]);");
	}

	return jsonFeedback(FEEDBACK_TYPE_NORMAL, "Done");
}

function deleteKid($nid) {
	global $link;
	if (!empty($nid)) {
		$query = "DELETE FROM `parents` WHERE `kid_id`='$nid';";
		$query .= "DELETE FROM `kids` WHERE `nid`='$nid';";
		$result = mysqli_multi_query($link, $query);
		if (!$result) {
			return jsonFeedback(FEEDBACK_TYPE_ERROR, "Couldn't delete the record!");
		}
		return jsonFeedback(FEEDBACK_TYPE_NORMAL, "Record deleted successfully.");
	}
}

function instantiateSession($device_id) {
	global $link;
	/*
	$query = "SELECT `type` FROM `devices` WHERE `id`=$device_id;";
	$result = mysqli_query($link, $query);
	$school = false;
	if ($result and mysqli_num_rows($result) > 0) {
		$row = mysqli_fetch_assoc($result);
		$school = $row['type'] == 2;
	}

	if ($school) {
	*/
	$query = "SELECT * FROM `sessions` WHERE DATE(`date`)=CURDATE() AND `device_id`=$device_id;";
	$result = mysqli_query($link, $query);
	if ($result and mysqli_num_rows($result) > 0) {
		$row = mysqli_fetch_assoc($result);
		return $row['id'];
	}
	// }
	$query = "INSERT INTO `sessions` SET `device_id`=$device_id;";
	$result = mysqli_query($link, $query);
	if (!$result) {
		echo mysqli_error($link);
		return -1;
	} else {
		return mysqli_insert_id($link);
	}
}

function readKid($kid_ids, $session_id, $device_id) {
	global $link;

	$status = array();
	$deleted = 0;
	$inserted = 0;
	foreach ($kid_ids as $nid)  {
		// upload to read tags
		$query = "INSERT INTO `readings` SET `device_id`=$device_id, `value`='$nid';";
		$result = mysqli_query($link, $query);
		// -- end --
		$query = "SELECT * FROM `sessionKids` WHERE `kid_id`='$nid' AND `session_id`=$session_id;";
		$result = mysqli_query($link, $query);
		$insert;
		if ($result && mysqli_num_rows($result) > 0) {
			$query = "DELETE FROM `sessionKids` WHERE `kid_id`='$nid' AND `session_id`=$session_id;";
			$insert = false;
		} else {
			$query = "INSERT INTO `sessionKids` SET `kid_id`='$nid', `session_id`=$session_id;";
			$insert = true;
		}

		$result = mysqli_query($link, $query);
		if ($result) {
			if ($insert) {
				$inserted++;
			} else {
				$deleted++;
			}
		}
	}
	if ($inserted == 0 && $deleted == 0) {
		return "error";
	}
	$o1 = "";
	$o2 = "";
	if ($inserted > 0) {
		$o1 = $inserted . " " . ($inserted > 1 ? "kids" : "kid") . " entered";
	}
	if ($deleted > 0) {
		$o2 = $deleted . " " . ($deleted > 1 ? "kids" : "kids") . " left";
	}
	return $o1 . ($inserted > 0 && $deleted > 0 ? " and " : "") . $o2;
}

function uploadRecord($session_Id, $latitude, $longitude) {
	global $link;
	$query = "INSERT INTO `records` SET `session_Id`=$session_Id, `latitude`=$latitude, `longitude`=$longitude;";
	$result = mysqli_query($link, $query);
	if ($result) {
		return "Recorded";
	} else {
		return "error";
	}
}

function generateTimelineRecords($user_id = -1, $since = "today") {
	global $link;

	$query = "SELECT `kids`.* FROM `parents` INNER JOIN `kids` ON `parents`.`kid_id`=`kids`.`nid`";
	$query .= $user_id > -1 ? " WHERE `parents`.`user_id`=$user_id;" : ";";
	$result = mysqli_query($link, $query);
	$kids = array();
	if ($result) {
		$kids = mysqli_fetch_all($result, MYSQLI_ASSOC);
	}

	$records = array();
	$vc = "";
	$first = true;
	foreach ($kids as $kid) {
		$records[$kid["nid"]] = array();
		if (!$first) {
			$vc .= " OR ";
		}
		$vc .= "`value`=$kid[nid]";
		$first = false;
	}
	$vc = strlen($vc) > 0 ? "($vc)" : "1";
	$condition = "$vc AND DATE(`time`)=CURDATE()";
	$query =
	"SELECT `readings`.`id` AS reading_id, `device_id`, `value`, UNIX_TIMESTAMP(`time`) AS `timestamp`, `holder_name`, `d`.`t` AS `t`
	FROM `readings` INNER JOIN
	(SELECT DISTINCT `devices`.`id` AS `i`,
	 CASE `devices`.`type`
		WHEN 1 THEN `trans`.`num_plate`
		WHEN 2 THEN `schools`.`name`
		ELSE 'none'
	END AS `holder_name`,
	`devices`.`type` AS `t`
	FROM `devices`, `schools`, `trans`
	WHERE
	`devices`.`type`=1 AND `trans`.`device_id`=`devices`.`id`
	OR
	`devices`.`type`=2 AND `schools`.`device_id`=`devices`.`id`
	ORDER BY `devices`.`id`) AS `d`
	ON `readings`.`device_id`=`d`.`i`
	WHERE $condition
	ORDER BY `timestamp`";

	$result = mysqli_query($link, $query);
	$readings = array();
	if ($result) {
		$triggers = array();
		while ($row = mysqli_fetch_assoc($result)) {
			if (isset($triggers[$row['value']]) and isset($triggers[$row["value"]][$row["device_id"]])) {
				$triggers[$row["value"]][$row["device_id"]] = !$triggers[$row["value"]][$row['device_id']];
			} else {
				$triggers[$row["value"]][$row["device_id"]] = true;
			}
			$readings[$row['value']][] = array("entered" => $triggers[$row['value']][$row["device_id"]] ? true : false, "device_id" => $row["device_id"], "type" => $row["t"], "time" => $row["timestamp"], "holder_name" => $row["holder_name"], "reading_id" => $row["reading_id"]);
		}
	} else {
		echo mysqli_error($link);
	}

	foreach ($readings as $k => $v) {
		foreach ($v as $a) {
			if ($since == "today" || ((int) $a["time"]) >= $since) {
				$records[$k][] = array("reading_id" => $a["reading_id"], "type" => $a["type"], "holder_name" => $a["holder_name"], "time" => (int) $a["time"], "entered" => $a["entered"] ? true : false);
			}
		}
	}
	return $records;
}
?>
