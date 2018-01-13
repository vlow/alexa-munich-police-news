# alexa-munich-police-news
The "Münchner Polizei Pressemeldungen"-flash-briefing-skill for the Amazon Alexa.

## Introduction
The police department of Munich releases insights about their recent operations publicly on their [website](http://www.polizei.bayern.de/muenchen/news/presse/aktuell/). This project reads the most
recent report into a cache table and returns it from there when called by the Amazon servers.

## Functional Principle
The polizeiScraper is called every 30 minutes by a CloudWatch Event and reads the press report website
to find the newest report. It then transforms the report into the JSON format used for flash-briefing-skills
and saves the result to a DynamoDB table. On every run, the singular entry in the table is overwritten.

The Amazon servers call the polizeiAdapter about once an hour. The polizeiAdapter returns the current entry
from the DynamoDB table. The Amazon servers seem to cache the result for an undefined amount of time.

## Caveats
According to the Amazon developer documentation, only the last five entries are considered by flash-briefing-skills.
This means that the skill is not able to deliver all messages in the press report, if it consists of more than five events.
The Alexa App offers a "Read more..." option which will take the user to the full list of all messages.

## Copyright Notice
All content provided by this skill is property of the Munich police department:

```
Polizeipräsidium München
Ettstr. 2
80333 München
Tel: 089/2910-0
```

The author's consent to the usage of the press reports is publicly available an can be viewed here:
https://twitter.com/PolizeiMuenchen/status/950999318051344384

Other than providing the content processed by this skill, the police department has no further affiliation with this software.

The source code is available under GPLv3.
