/* 
 * This file is part of the alexa-munich-police-news distribution
 * (https://github.com/vlow/alexa-munich-police-news).
 * Copyright (c) 2018 Florian Engel / Moritz Kammerer.
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * This adapter reads pre-formatted JSON from a DynamoDB table and returns
 * it to the caller. It is generic in that way and could be used for other
 * flash briefing skills by only adjusting the TABLE_NAME constant.
 */

// Technical parameters
const DYNAMO_DB_REGION = 'eu-west-1';
const DYNAMO_DB_API_VERSION = '2012-10-08';
const TABLE_NAME = 'polizeiPressemeldungen';

// The DynamoDB table has only one entry, which contains
// the pre-rendered JSON, returned by this adapter.
//
// The content is updated regularly by the polizeiScraper.
const ENTRY_ID = '0';

exports.handler = (event, context, callback) => {
    // configure db connection
    let AWS = require('aws-sdk');
    AWS.config.update({ region: DYNAMO_DB_REGION });
    let ddb = new AWS.DynamoDB({ apiVersion: DYNAMO_DB_API_VERSION });
    
    // query cache tabe
    let params = {
        TableName: TABLE_NAME,
        Key: {
            'id': { N: ENTRY_ID },
        },
    };
    ddb.getItem(params, function(err, data) {
        if (err) {
		// we do not expose the technical error to the caller,
		// but we should still be able to find out about it...
                console.log("Error", err);
		    // return error
		    let response = {
			statusCode: 500,
			headers: {
			    "Content-Type": "application/json"
			},
			body: JSON.stringify({
			    'message': 'Error while querying database.'
			})
		    };
		    callback(null, response);
        }
        else {
            // return cached police news     
            let response = {
                statusCode: 200,
                headers: {
                    "Content-Type": "application/json"
                },
                body: data.Item['content']['S']
            };
            callback(null, response);
        }
    });
};

