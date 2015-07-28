function AppViewModel() {
	
	var urlInactiveImages = "https://i43pc164.ipd.kit.edu/PSESoSe15Gruppe3/mensa/api/admin/inactiveImages/";
	var urlInactiveMealData= "https://i43pc164.ipd.kit.edu/PSESoSe15Gruppe3/mensa/api/admin/mergingMeals/";
	this.mealDataArr = ko.observableArray();
	this.photosDataArr = ko.observableArray();
	
	var headers = 
	{
    "Authorization": "Basic YWRtaW46bWVuc2F4"
  	};
	
	this.selectedMeal = ko.observable(0);
	this.mergingMode = ko.observable(true);

	var self = this;

	$.ajax({
		type: 'GET',
		url: urlInactiveImages,
	    headers: headers
	}).done(function(data){
		$.each(data, function (i,item) {
			self.photosDataArr.push(item);
		});
	});


	$.ajax({
		type: 'GET',
		url: urlInactiveMealData,
		headers: headers
	}).done(function(data){
		var tempArrayMeal = [];
		$.each(data, function (i, obj) {
			$.each(obj, function (j, item) {
			tempArrayMeal.push(item);
				if(obj.length === (j + 1)){
					self.mealDataArr.push(tempArrayMeal);
					tempArrayMeal.length = 0;	
				}
			});
		});
	});
	
	self.markActice = function(meal){	
		self.selectedMeal(meal);
	};

	self.setActiveMerge = function(active, txt){
		if(self.mergingMode !== active){
			self.mergingMode(active);
		}

		if(active){
			$("#photoLi").removeClass('active');
			$("#mergeLi").addClass('active');
		}else {
			$("#mergeLi").removeClass('active');
			$("#photoLi").addClass('active');
		}
	};
	
	self.acceptMerge = function(meal){
		var acceptMergeUrl = "https://i43pc164.ipd.kit.edu/PSESoSe15Gruppe3/mensa/api/admin/finalizeMerge";	
		var inactiveDataId = meal.data.id;
		var data = {
			"mealDataId" : inactiveDataId,
			"approved" : true
		};
		$.ajax({
            data: JSON.stringify(data),
            type: 'POST',
            contentType:"application/json; charset=utf-8",
            headers: headers,
            url: acceptMergeUrl,
            dataType: 'json',
            success: function(data2){
                alert("Successfully accepted the merge!");
                location.reload();
            }
    	}); 
		location.reload();
	};

	self.declineMerge = function(meal){
		var acceptMergeUrl = "https://i43pc164.ipd.kit.edu/PSESoSe15Gruppe3/mensa/api/admin/finalizeMerge";	
		var inactiveDataId = meal.data.id;
		var data = {
			"mealDataId" : inactiveDataId,
			"approved" : false
		};
		$.ajax({
            data: JSON.stringify(data),
            type: 'POST',
            contentType:"application/json; charset=utf-8",
            headers: headers,
            url: acceptMergeUrl,
            dataType: 'json',
            success: function(){
                alert("Pending merge deleted!");
                //console.log(data2);
                location.reload();
            },
            fail: function(resp){
            	console.log(resp);
            }
    	}); 
		location.reload();

	};

	self.acceptPhoto = function(photo){
		var acceptMergeUrl = "https://i43pc164.ipd.kit.edu/PSESoSe15Gruppe3/mensa/api/admin/finalizeImagePost";	
		var imageId = photo.imageid;
		var data = {
			"imageId" : imageId,
			"approved" : true
		};
		$.ajax({
            data: JSON.stringify(data),
            type: 'POST',
            headers: headers,
            contentType:"application/json; charset=utf-8",
            url: acceptMergeUrl,
            dataType: 'json',
            success: function(data2){
                alert("Successfully accepted the photo!");
                location.reload();
            },
            fail: function(){
            	location.reload();
            }
    	}); 
		location.reload();
	};

	self.declinePhoto = function(photo){
		var acceptMergeUrl = "https://i43pc164.ipd.kit.edu/PSESoSe15Gruppe3/mensa/api/admin/finalizeImagePost";	
		var imageId = photo.imageid;
		var data = {
			"imageId" : imageId,
			"approved" : false
		};
		$.ajax({
            data: JSON.stringify(data),
            headers: headers,
            type: 'POST',
            contentType:"application/json; charset=utf-8",
            url: acceptMergeUrl,
            dataType: 'json',
            success: function(data2){
                alert("Photo deleted!");
                location.reload();
            },
            fail: function(resp){
            	console.log(resp);
            }
    	}); 
            location.reload();
	};
}

// Activates knockout.js
ko.applyBindings(new AppViewModel());
