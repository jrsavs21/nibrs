/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var isAdvancedUpload = function() {
	var div = document.createElement('div');
	return (('draggable' in div) || ('ondragstart' in div && 'ondrop' in div)) && 'FormData' in window && 'FileReader' in window;
}();

$(function(){
	
	var $form = $(".box");
	
	if (isAdvancedUpload){
		
		var droppedFiles = false;
		var dropzone = $("#dropzone");
		
		//TODO should not need the upload function anymore. Will double check with more testing. -hw 
		var upload = function(files){
			
		  console.log(files);	
		  var ajaxData = new FormData($form.get(0));
		  console.log("file0:" + files[0]);
		  if (files) {
			    $.each( files, function(i, file) {
			        ajaxData.append( 'file', file );
			      });
		  }
		  console.log(ajaxData);	

		  $.ajax({
		    url: $form.attr('action'),
		    type: $form.attr('method'),
		    data: ajaxData,
		    cache: false,
		    contentType: false,
		    processData: false,
		    complete: function() {
		      $form.removeClass('is-uploading');
		    },
		    success: function(data) {
		    	console.log(data);
		        $form.addClass( data.success == true ? 'is-success' : 'is-error' );
	            document.open();
	            document.write(data);
	            document.close();
		    }
		  });
		}
		
		$( "#dropzone" ).on( "dragover dragenter", function() {
			  $(this).addClass("dragover");
			  return false;
		})
		.on( "dragleave dragend drop", function() {
			$(this).removeClass("dragover");
			return false;
		})
		.on('drop', function(e) {
			e.preventDefault();
		    droppedFiles = e.originalEvent.dataTransfer.files;
		    upload(droppedFiles);
		});
		
	}
	else{
		
	}
	
	$form.on('submit', function(e) {
	  if ($form.hasClass('is-uploading')) return false;

	  $form.addClass('is-uploading').removeClass('is-error');

	  e.preventDefault();

	  var ajaxData = new FormData($form.get(0));

	  if (droppedFiles) {
	    $.each( droppedFiles, function(i, file) {
	      ajaxData.append( $("#file").attr('name'), file );
	    });
	  }

	  $.ajax({
	    url: $form.attr('action'),
	    type: $form.attr('method'),
	    data: ajaxData,
	    cache: false,
	    contentType: false,
	    processData: false,
	    complete: function() {
	      $form.removeClass('is-uploading');
	    },
	    success: function(data) {
	    	console.log(data.success);
	    	console.log(data);
	    	$form.addClass( data.success == true ? 'is-success' : 'is-error' );
    		document.open();
			document.write(data);
			document.close();
	    },
	    error: function() {
	      // Log the error, show an alert, whatever works for you
	    }
	  });
	});
	
	$("#file").on('change', function(e) { 
		$("#file").removeAttr("form");
		$form.trigger('submit');
	});
});