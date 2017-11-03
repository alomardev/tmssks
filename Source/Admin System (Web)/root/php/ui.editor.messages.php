<div class="input-group">
	<label for="subject">Subject</label>
	<input name="subject" type="text" placeholder="Subject" readonly required input-prop-readable-new>
</div>
<div class="input-group">
	<label for="body">Message</label>
	<textarea name="body" readonly required input-prop-readable-new placeholder="Content" rows="10"></textarea>
</div>
<div class="input-group">
	<select name="from_id" input-prop-combo="./api/get-users.php?role=1|id|name|---">
	</select>
	<input type="hidden" name="to_id">
</div>
