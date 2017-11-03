<script>
	var loadUrl = <?php echo "'$editorLoad'"; ?>;
	var deleteUrl = <?php echo "'$editorDelete'"; ?>;
	var listItemProp = <?php echo "'$listItemProperty'"; ?>;
</script>
<div id="editor">
	<table>
		<thead>
			<tr>
				<td><h3><?php echo $editorListTitle ?></h3></td>
				<td><h3>Details<span id="new-attention" style="display: none; color: red;"> (new)</span></h3></td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>
						<div id="editor-list">
						</div>
				</td>
				<td>
					<form id="editor-form" action="<?php echo $editorSave; ?>">
						<?php include "$editorFormInputs"; ?>
					</form>
				</td>
			</tr>
			<tr>
				<td>
					<div id="editor-actions" class="clear-float">
						<a class='float-left icon-btn' onclick="onAddItem()"><div class='ic_add'></div></a>
						<a class='float-left icon-btn' onclick="onRemoveItem()"><div class='ic_remove'></div></a>
						<div id="entities-count"></div>
					</div>
				</td>
				<td>
				<div class="clear-float">
					<input id="editor-form-submit" class="float-right" type="submit" value="Save Record" disabled>
					<div id="editor-form-status" class="float-right"></div>
				</div>
			</td>
			</tr>
		</tbody>
		</table>
</div>
<?php $script=array("js/jquery-3.1.1.min.js", "js/editor.js") ?>
