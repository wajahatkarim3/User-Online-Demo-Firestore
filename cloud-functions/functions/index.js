const functions = require('firebase-functions');
//const TheFirestore = require('@google-cloud/firestore');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

const firestore = admin.firestore;

exports.onUserStatusChanged = functions.database
	.ref('/status/{userId}')
	.onUpdate(event => {
		const usersRef = firestore.collection('/users');
		
		return event.data.ref.once('value')
			.then(statusSnapshot => snapShot.val())
			.then(status => {
				if (status === 'offline'){
					// Set the firestore's doc's online value to false
					usersRef
						.doc(event.params.userId)
						.set({
							online: false
						}, {merge: true});
				}
			})
	});

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
