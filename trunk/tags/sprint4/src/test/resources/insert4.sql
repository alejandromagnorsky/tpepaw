--
-- PostgreSQL database dump
--

-- Started on 2011-07-10 12:48:42 ART

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

COPY issue_followers (issueid, userid) FROM stdin;
24	2
22	2
7	2
13	2
12	4
21	4
21	7
7	7
13	4
9	4
\.

INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Blasphemer', 'In the quest for shudders I was as the absence', 2, 2, 'Completed', 6, '2011-07-10 18:36:16.373', 3787, 'High', 'WontResolve', 'Technique');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Blackwinged', 'We sail the seas, of negativity', 1, 2, 'Open', 6, '2011-07-10 18:32:33.243', 1623, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Gather For Attack On The', 'Pearly Gates', 3, 2, 'Open', 6, '2011-07-10 18:33:42.492', 245, 'High', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Black Victory Of Death', 'To breed the brethren of black fire', 1, 2, 'Open', 6, '2011-07-10 18:34:57.103', 7568, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Majestic Desolate Eye', NULL, 4, 2, 'Open', 6, '2011-07-10 18:35:25.363', 1201, 'Low', NULL, 'NewFeature');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Ravnajuv', 'I dype juvet, ein stad i Noregs land', 3, 2, 'Open', 6, '2011-07-10 18:37:04.361', 7568, 'Critical', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('The Serpents Harvest', 'And from the vast towers of the underworld', 3, 2, 'Open', 6, '2011-07-10 18:37:50.765', 1623, 'High', NULL, 'Improvement');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Disintegrator/Incinerator', 'One thousand miles per hour', 4, 2, 'Open', 3, '2011-07-10 18:51:06.694', 13502, 'Critical', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Nocturnal Hell', 'Into the dream world slowly I creep', 2, 2, 'Open', 3, '2011-07-10 18:52:13.574', NULL, 'High', NULL, 'Improvement');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Earth''s Last Picture', 'Total Death,  Darkthrone', 2, 2, 'Completed', 6, '2011-07-10 18:31:10.772', 1201, 'Low', 'Resolved', 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('F.O.D.', 'Deep in the coffin,death prevails', 2, 2, 'Completed', 3, '2011-07-10 18:56:41.84', NULL, 'Critical', 'Duplicated', 'Technique');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Tortured Souls', 'Rampaging through graveyards', 2, 2, 'Open', 3, '2011-07-10 18:57:44.396', NULL, 'Critical', NULL, 'NewFeature');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Parasites', 'Like cancer he can kill from within', 4, 2, 'Open', 3, '2011-07-10 18:58:11.663', 1201, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('The Curse', 'To summon up the Gods of wrath', NULL, 2, 'Open', 3, '2011-07-10 18:58:45.138', 3247, 'Trivial', NULL, 'Error');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Maim To Please', 'Shaking badly from your fear', 2, 2, 'Open', 3, '2011-07-10 19:00:04.447', 7568, 'Low', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Strappado', 'Prepare yourself for torture', 2, 2, 'Open', 3, '2011-07-10 18:59:30.946', 245, 'Critical', NULL, 'Technique');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('One Foot In The Grave', 'One foot in the grave', 4, 2, 'Open', 3, '2011-07-10 19:00:37.506', NULL, 'High', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Tyrant Of Hell', 'Set ablaze the gates of Hades', NULL, 2, 'Open', 3, '2011-07-10 19:01:55.814', 2768, 'Low', NULL, 'Improvement');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Death Dealer', 'I am death, here''s my deal
I''ve chosen your carcass for my meal
Necrocannibal feast of flesh
I''m the dealer give me death', 2, 2, 'Open', 3, '2011-07-10 19:02:30.754', 2768, 'Critical', NULL, 'Error');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Tales Of The Macabre', 'Listen to my tales of mayhem', 5, 2, 'Open', 3, '2011-07-10 19:03:40.412', 1623, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('The Exorcist', 'Demons dog domain', 5, 2, 'Open', 3, '2011-07-10 19:05:41.727', 3246, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Pentagram', 'There''s a sacred city not far from here', 4, 2, 'Open', 3, '2011-07-10 19:08:44.273', 2768, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Burning In Hell', NULL, 5, 2, 'Open', 3, '2011-07-10 19:09:19.751', 2768, 'Critical', NULL, 'Improvement');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Evil Warriors', 'But if you want to see them
You''ll have to look down', 2, 2, 'Open', 3, '2011-07-10 19:10:09.921', 3246, 'Trivial', NULL, 'Error');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Seven Churches', 'Seven churches down in hell', 2, 2, 'Open', 3, '2011-07-10 19:10:42.603', 1201, 'Critical', NULL, 'Technique');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Satan''s Curse', 'From the sky it falls', 2, 2, 'Open', 3, '2011-07-10 19:11:14.318', 7568, 'Low', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Holy Hell', 'Devil''s water starts to flood', 5, 2, 'Open', 3, '2011-07-10 19:11:51.547', NULL, 'Low', NULL, 'Technique');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Twisted Minds', 'Twisting my mind, insane', 4, 2, 'Open', 3, '2011-07-10 19:12:33.458', 245, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Fallen Angel', 'Outcasts will rebel', 5, 2, 'Open', 3, '2011-07-10 19:12:58.8', NULL, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Death Metal', 'Arise from the dead
Attack from the grave
The killing won''t stop ''til first light
We''ll bring you to hell', 2, 2, 'Open', 3, '2011-07-10 19:14:12.667', NULL, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Visions Of Mortality', 'My life turns into sand', 3, 2, 'Open', 4, '2011-07-10 19:21:14.762', 7568, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Morbid Tales', 'By burial this night', 1, 2, 'Open', 4, '2011-07-10 19:22:29', 1623, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Dethroned Emperor', 'See the portal, gate to madness', 2, 2, 'Completed', 4, '2011-07-10 19:22:00.921', 1201, 'Normal', 'Duplicated', 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Into The Crypt Of Rays', 'Years of plead, behind the walls', 2, 2, 'Completed', 4, '2011-07-10 19:20:16.253', NULL, 'High', 'Irreproducible', 'Technique');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Circle Of The Tyrants', 'After the battle is over', 3, 2, 'Open', 4, '2011-07-10 19:24:30.41', NULL, 'Low', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Procreation Of The Wicked', 'You are blind - Deny the sun and light
Whose are the objections - From where come the doubts', 3, 2, 'Open', 4, '2011-07-10 19:25:10.785', 2768, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Return To The Eve', 'Dreaming eyes
Hope to return
As shadows fall onto
Distorted paths', NULL, 2, 'Open', 4, '2011-07-10 19:25:41.297', 245, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Danse Macabre', 'Instrumental', 1, 2, 'Open', 4, '2011-07-10 19:27:25.201', 4869, 'High', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Nocturnal Fear', NULL, 3, 2, 'Open', 4, '2011-07-10 19:27:46.442', NULL, 'Normal', NULL, 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Visual Aggression', 'Thousand decades in vain
(Again) they strive for final completion', 2, 2, 'Completed', 4, '2011-07-10 19:28:48.866', 1201, 'Low', 'Resolved', 'Issue');
INSERT INTO issue (title, description, assigneduser, reporteduser, state, projectid, creationdate, estimatedtime, priority, resolution, type) VALUES ('Suicidal Winds', 'Listen
As the mist is rising
And uncovers all that`s lost
Try and mend what you can barely feel', 2, 2, 'Completed', 4, '2011-07-10 19:28:22.601', 1201, 'High', 'WontResolve', 'NewFeature');

COPY defaultviews (userid, view) FROM stdin;
4	Filters
4	Versions
2	Issues
2	Filters
2	Versions
5	RecentChanges
4	RecentChanges
5	Issues
5	Versions
\.

