TSG = window.TSG || {};

TSG.StatisticsBackend = function(options) {

	var _userLogged = $('#txtLogin').val();
    var _stats = { won: 0, lost: 0 };
    var _currentStats = { won: 0, lost: 0 };
    _loadStatistics();

    /**
     * Loads statistics.
     * @return {object} statistics like { won: 0, lost: 0 }
     */
    function _loadStatistics() {
    	$.ajax({
			url : 'http://localhost:8080/stickgame/service/statistics/' + _userLogged,
			type : 'GET',
			contentType : 'application/json',
			success : function(response) {
				if(response != null) {
					_stats = { won: response.totalWon, lost: response.totalLost };
				} else {
					_stats = { won: 0, lost: 0 };
				}
				_statsChanged();
			}
		});
    }

    /**
     * Stores current statistics into backend
     */
    function _saveStatistics() {
        $.ajax({
			url : 'http://localhost:8080/stickgame/service/statistics/' + _userLogged,
			type : 'PUT',
			contentType : 'application/json',
			data : JSON.stringify(convertStats()),
			success : function(response) {
				_stats = { won: response.totalWon, lost: response.totalLost };
				_currentStats = { won: 0, lost: 0 };
				$('#startSticks').val('');
				_statsChanged();
			}
		});
    }

    function convertStats() {
    	var statistic = { totalWon: _currentStats.won, totalLost: _currentStats.lost };
    	return statistic;
    }
    
    function _statsChanged() {
      if (options.onChange) {
        options.onChange(_getStats());
      }
    }

    function _gameFinished(won) {
        if (won) {
        	_currentStats.won++;
        } else {
        	_currentStats.lost++;
        }
        _saveStatistics();
        _statsChanged();
    }

    function _getStats() {    	
        return { won: _stats.won, lost: _stats.lost, played: _stats.won + _stats.lost };
    }

    return {
        gameFinished: _gameFinished,
        getStats: _getStats
    };
};
