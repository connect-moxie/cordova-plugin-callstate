var CallState = {
    callState: function(success, error) {
        error = error || this.error;
        cordova.exec(success, error, 'CallState', 'callState', []);
    },

    error: function() {
        console.log("W: CallState error callback not defined.");
    }
};

module.exports = CallState;
