<?php
require_once "../php/app.php";
openSession();

$user = array();
$user['id'] = $_POST['id'];
$user['name'] = $_POST['name'];
$user['nid'] = $_POST['nid'];
$user['username'] = $_POST['username'];
$user['password'] = $_POST['password'];
$user['email'] = $_POST['email'];
$user['phone1'] = $_POST['phone1'];
$user['phone2'] = $_POST['phone2'];

$user['role'] = $session['role'] == ROLE_ROOT_ADMIN ? ROLE_SCHOOL_ADMIN : ROLE_PARENT;

echo saveUser($user);
?>
