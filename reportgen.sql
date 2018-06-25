-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jun 25, 2018 at 11:07 AM
-- Server version: 5.7.9
-- PHP Version: 5.6.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `reportgen`
--

-- --------------------------------------------------------

--
-- Table structure for table `base`
--

DROP TABLE IF EXISTS `base`;
CREATE TABLE IF NOT EXISTS `base` (
  `BaseID` int(11) NOT NULL AUTO_INCREMENT,
  `Base` varchar(50) NOT NULL,
  PRIMARY KEY (`BaseID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `base`
--

INSERT INTO `base` (`BaseID`, `Base`) VALUES
(1, 'Line Setup'),
(2, 'Metal Detector'),
(3, 'Back Flush'),
(4, 'Winder Wastage'),
(5, 'Wrapper Wastage'),
(6, 'Diameter Variation'),
(7, 'Colour Variation'),
(8, 'Crimp Variation'),
(9, 'Material Quality'),
(10, 'Process Issue'),
(11, 'Technical Fault'),
(12, 'Workers Fault'),
(13, 'Technicians Fault'),
(14, 'Supervisory Fault'),
(15, 'Planning Fault'),
(16, 'Other'),
(17, 'Cutter wastage'),
(18, 'Rewrape'),
(19, 'Colour variation'),
(20, 'Diameter variation');

-- --------------------------------------------------------

--
-- Table structure for table `downtimes`
--

DROP TABLE IF EXISTS `downtimes`;
CREATE TABLE IF NOT EXISTS `downtimes` (
  `Id` int(10) NOT NULL AUTO_INCREMENT,
  `Reason` varchar(50) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `downtimes`
--

INSERT INTO `downtimes` (`Id`, `Reason`) VALUES
(1, 'Heating'),
(2, 'Item Changing & Cleaning '),
(3, 'Line Setup'),
(4, 'Winder Breakdown'),
(5, 'Cutter Breakdown '),
(6, 'Process Issues'),
(7, 'Machine Line Technical Errors'),
(8, 'Material Delay & Quality'),
(9, 'Sample Production'),
(10, 'Power Cut/Water Cut'),
(11, 'Personal Issues'),
(12, 'Panning issues');

-- --------------------------------------------------------

--
-- Table structure for table `packmachinetime`
--

DROP TABLE IF EXISTS `packmachinetime`;
CREATE TABLE IF NOT EXISTS `packmachinetime` (
  `Id` varchar(25) NOT NULL,
  `PartNumber` varchar(15) NOT NULL,
  `TimeFrom` float NOT NULL,
  `TimeTo` float NOT NULL,
  `Mins` int(4) NOT NULL,
  `InsertDate` date NOT NULL,
  `ReportDate` date NOT NULL,
  `Supervisor` varchar(20) NOT NULL,
  `Technician` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `packoee`
--

DROP TABLE IF EXISTS `packoee`;
CREATE TABLE IF NOT EXISTS `packoee` (
  `Id` varchar(60) NOT NULL,
  `CutLength` int(25) NOT NULL,
  `SPO` float NOT NULL,
  `Reject` float NOT NULL,
  `Output` float NOT NULL,
  `aTime` float NOT NULL,
  `pdTime` float NOT NULL,
  `dTime` float NOT NULL,
  `oTime` float NOT NULL,
  `irRate` float NOT NULL,
  `aRate` float NOT NULL,
  `pRate` float NOT NULL,
  `qRate` float NOT NULL,
  `Oee` float NOT NULL,
  `reportDate` date NOT NULL,
  `insertDate` date NOT NULL,
  `Shift` varchar(12) NOT NULL,
  `Supervisor` varchar(25) NOT NULL,
  `Technician` varchar(25) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `part2`
--

DROP TABLE IF EXISTS `part2`;
CREATE TABLE IF NOT EXISTS `part2` (
  `Item` varchar(28) DEFAULT NULL,
  `SWOBoth` int(3) DEFAULT NULL,
  `SWOSingle` int(3) DEFAULT NULL,
  `CWPre` decimal(10,4) DEFAULT NULL,
  `CWKg` varchar(4) DEFAULT NULL,
  `SPOBoth` int(3) DEFAULT NULL,
  `SPOSingle` decimal(5,2) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `part2`
--

INSERT INTO `part2` (`Item`, `SWOBoth`, `SWOSingle`, `CWPre`, `CWKg`, `SPOBoth`, `SPOSingle`) VALUES
('GRE-PE-178-0.013-6-FL-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('RED-PE-178-0.028-0-NF-R', 235, NULL, '0.0640', '15.0', 220, NULL),
('RED-PE-188-0.028-0-NF-R', 235, NULL, '0.0765', '17.9', 217, NULL),
('BLK-PE-220-0.040-0-NF-O', 235, 155, '0.0456', '10.7', 224, '144.28'),
('BLK-PE-160-0.040-0-NF-O', 235, 155, '0.0456', '10.7', 224, '144.28'),
('SIL-PE-178-0.016-4-FL-R', 215, NULL, '0.0640', '13.7', 201, NULL),
('ORG-PE-171-0.013-4-PF-R', 190, NULL, '0.1000', '19.0', 171, NULL),
('GRY-PE-171-0.013-4-PF-R', 190, NULL, '0.1000', '19.0', 171, NULL),
('GRY-PE-178-0.016-0-FL-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('GRE-PE-177-0.016-0-FL-R', 225, NULL, '0.0684', '15.3', 210, NULL),
('MAR-PE-210-0.026-4-NF-R', 235, NULL, '0.1158', '27.2', 208, NULL),
('MAR-PE-178-0.031-0-NF-R', 235, NULL, '0.0640', '15.0', 220, NULL),
('BLK-PE-385-0.030-0-FL-R', 235, NULL, '0.0544', '12.7', 222, NULL),
('BLK-PE-178-0.026-0-NF-R', 235, NULL, '0.0640', '15.0', 220, NULL),
('BLK-PE-223-0.040-2-NF-R', 235, 155, '0.0611', '14.3', 221, '140.64'),
('BLK-PE-175-0.014-4-NF-R', 200, NULL, '0.0789', '15.7', 184, NULL),
('BLK-PE-080-0.016-0-FL-R', 225, NULL, '0.0456', '10.2', 170, NULL),
('BLK-PE-175-0.014-6-NF-O', 225, NULL, '0.0789', '17.7', 207, NULL),
('BLK-PE-178-0.022-0-NF-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('NAT-NY-125-0.022-4-NF-R', 150, NULL, '0.0789', '11.8', 138, NULL),
('YEL-PE-255-0.031-0-FL-R', 235, NULL, '0.1075', '25.2', 210, NULL),
('YEL-PE-270-0.031-0-FL-R', 235, NULL, '0.0550', '12.9', 222, NULL),
('YEL-PE-325-0.031-0-FL-R.', 235, NULL, '0.0877', '20.6', 214, NULL),
('YEL-PE-385-0.031-0-FL-R', 235, NULL, '0.0544', '12.7', 222, NULL),
('WHI-PE-125-0.010-0-NF-R', 190, NULL, '0.0789', '14.9', 175, NULL),
('YEL-PE-125-0.010-0-NF-R', 190, NULL, '0.0789', '14.9', 175, NULL),
('GRY-PE-178-0.011-6-FL-R', 200, NULL, '0.0640', '12.8', 187, NULL),
('BLU-PB-125-0.015-6-NF-R', 225, NULL, '0.0789', '17.7', 207, NULL),
('BLU-PE-125-0.010-0-NF-R', 190, NULL, '0.0789', '14.9', 175, NULL),
('GRE-PE-125-0.010-0-NF-R', 190, NULL, '0.0789', '14.9', 175, NULL),
('GRE-PE-165-0.035-0-NF-R', 235, NULL, '0.0737', '17.3', 218, NULL),
('RED-PE-175-0.014-4-NF-R', 200, NULL, '0.0789', '15.7', 184, NULL),
('CRE-PE-180-0.014-0-NF-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('BLU-PE-175-0.014-4-NF-R', 200, NULL, '0.0789', '15.7', 184, NULL),
('GRE-PE-175-0.014-4-NF-R', 200, NULL, '0.0789', '15.7', 184, NULL),
('GRE-PE-110-0.016-0-NF-R', 225, NULL, '0.0737', '16.5', 208, NULL),
('WHI-PP-067-0.013-0-NF-O', 160, NULL, '0.0596', '9.54', 135, NULL),
('WHI-PP-057-0.013-0-NF-O', 160, NULL, '0.0600', '9.60', 125, NULL),
('BLK-PE-060-0.007-0-NF-R', 110, NULL, '0.0737', '8.11', 102, NULL),
('BLK-PE-180-0.008-0-NF-R', 125, NULL, '0.0526', '6.58', 118, NULL),
('BLK-PE-275-0.030-0-NF-R', 235, NULL, '0.1316', '30.9', 204, NULL),
('BLK-PE-110-0.030-0-NF-O', 235, NULL, '0.0737', '17.3', 218, NULL),
('BLU-PE-385-0.030-0-NF-R', 235, NULL, '0.0544', '12.7', 222, NULL),
('BLK-PE-175-0.023-4-NF-O', 235, NULL, '0.0789', '18.5', 216, NULL),
('GRL-PE-145-0.010-6-FL-R', 190, NULL, '0.0840', '15.9', 174, NULL),
('BLU-PE-1300-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('BLU-PE-140-0.014-0-FL-R', 225, NULL, '0.0667', '15.0', 210, NULL),
('GRE-PE-140-0.014-4-FL-R', 190, NULL, '0.0667', '12.6', 177, NULL),
('BLU-PE-145-0.016-0-FL-R', 225, NULL, '0.0840', '18.9', 206, NULL),
('BLK-PE-225-0.015-0-FL-TR', 225, NULL, '0.0526', '11.8', 213, NULL),
('BLK-PE-170-0.010-6-NF-R', 190, NULL, '0.0650', '12.3', 178, NULL),
('RED-PE-100-0.015-0-NF-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('RED-PE-170-0.031-0-NF-R', 235, NULL, '0.0500', '11.7', 223, NULL),
('RED-PE-169-0.035-0-NF-R', 235, NULL, '0.0512', '12.0', 223, NULL),
('YEL-PE-385-0.031-0-FL-R', 235, NULL, '0.0700', '16.4', 219, NULL),
('YEL-PE-325-0.031-0-FL-R', 235, NULL, '0.0700', '16.4', 219, NULL),
('YEL-PE-090-0.026-4-NF-R', 235, NULL, '0.0789', '18.5', 180, NULL),
('YEL-PE-130-0.026-4-NF-R', 235, NULL, '0.0880', '20.6', 214, NULL),
('BLU-PE-125-0.025-4-NF-R', 235, NULL, '0.0789', '18.5', 216, NULL),
('RED-PE-110-0.024-0-NF-R', 225, NULL, '0.0737', '16.5', 208, NULL),
('BLU-PE-285-0.016-0-FL-R', 225, NULL, '0.1000', '22.5', 203, NULL),
('GRE-PE-285-0.016-0-FL-R', 225, NULL, '0.1000', '22.5', 203, NULL),
('GRE-PE-225-0.015-0-FL-TR', 210, NULL, '0.0526', '11.0', 199, NULL),
('GRE-PE-650-0.016-4-PF-R', 215, NULL, '0.0877', '18.8', 196, NULL),
('RED-PE-170-0.010-0-NF-R', 190, NULL, '0.0500', '9.50', 181, NULL),
('BLU-PE-225-0.020-0-FL-R', 235, NULL, '0.0526', '12.3', 223, NULL),
('NGRY-PE-172-0.022-4-NF-R', 235, NULL, '0.0947', '22.2', 213, NULL),
('BRO-PE-230-0.16-0-NF-R', 225, NULL, '0.1050', '23.6', 201, NULL),
('BRO-PE-060-0.016-0-NF-R', 225, NULL, '0.0500', '11.2', 160, NULL),
('BLK-PE-178-0.010-6-FL-R', 190, NULL, '0.0640', '12.1', 178, NULL),
('BLU-PE-185-0.016-4-NF-R', 215, NULL, '0.0910', '19.5', 195, NULL),
('BLU-PE-385-0.027-2-FL-R', 235, NULL, '0.0544', '12.7', 222, NULL),
('GRE-PE-325-0.031-0-FL-R', 235, NULL, '0.0900', '21.1', 214, NULL),
('BRO-PE-230-0.047-0-NF-R', 235, NULL, '0.1050', '24.6', 210, NULL),
('BLK-PE-250-0.047-0-NF-O', 235, NULL, '0.0785', '18.4', 217, NULL),
('SIL-PE-145-0.016-4-FL-R', 215, NULL, '0.0840', '18.0', 197, NULL),
('PUR-PE-170-0.014-0-NF-T', 200, NULL, '0.0700', '14.0', 186, NULL),
('BLU-PE-260-0.051-0-NF-O', 235, NULL, '0.0630', '14.8', 220, NULL),
('BLU-PE-320-0.051-0-NF-O', 235, NULL, '0.0630', '14.8', 220, NULL),
('BLU-PE-180-0.014-2-NF-R', 200, 100, '0.0526', '10.5', 189, '89.48'),
('PUR-PE-580-0.010-0-NF-R', 190, NULL, '0.1860', '35.3', 155, NULL),
('BRO-PE-230-0.047-0-NF-O', 235, NULL, '0.1050', '24.6', 210, NULL),
('ORG-540-0.014-0-FL-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('RED-PE-300-0.055-0-NF-R', 235, 155, '0.0526', '12.3', 223, NULL),
('RED-PE-170-0.040-0-NF-R', 235, NULL, '0.0700', '16.4', 219, NULL),
('RED-PE-170-0.035-0-NF-R', 235, NULL, '0.0700', '16.4', 219, NULL),
('RED-PE-175-0.035-0-NF-R', 235, NULL, '0.0789', '18.5', 216, NULL),
('GRY-PE-165-0.010-6-FL-R', 190, NULL, '0.0737', '14.0', 176, NULL),
('SIL-PE-178-0.016.4-NF-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('SIL-PE-245-0.016-0-FL-R', 225, NULL, '0.0544', '12.2', 213, NULL),
('BLK-PE-060-0.008-0-NF-R', 125, NULL, '0.0500', '6.25', 119, NULL),
('BLK-PE-180-0.010-0-NF-O', 190, NULL, '0.0530', '10.0', 180, NULL),
('BLK-PE-175-0.014-0-FL-R', 225, NULL, '0.0789', '17.7', 207, NULL),
('BLK-PE-145-0.022-0-FL-R', 235, NULL, '0.0840', '19.7', 215, NULL),
('BLK-PE-055-0.010-0-NF-R', 190, NULL, '0.0600', '11.4', 110, NULL),
('BLK-PP-182-0.023-4-NF-O', 160, NULL, '0.1100', '17.6', 142, NULL),
('CRE-PE-1300-0.018-4-NF-R', 225, NULL, '0.0900', '20.2', 205, NULL),
('CRE-PE-230-0.018-4-NF-R', 225, NULL, '0.1050', '23.6', 201, NULL),
('RED-PE-185-0.022-0-FL-R', 235, NULL, '0.0910', '21.3', 214, NULL),
('RED-PE-178-0.016-0-FL-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('CRE-PE-365-0.045-0-FL-R', 235, 155, '0.1035', '24.3', 211, '130.68'),
('RED-PE-220-0.055-0-NF-R', 235, NULL, '0.0740', '17.3', 218, NULL),
('GRE-PE-250-0.040-0-NF-R', 235, NULL, '0.0790', '18.5', 216, NULL),
('BLU-PE-250-0.047-0-NF-O', 235, NULL, '0.0790', '18.5', 216, NULL),
('BLU-PE-160-0.030-4-NF-R', 235, NULL, '0.0500', '11.7', 223, NULL),
('BLU-PE-165-0.030-4-NF-R', 235, NULL, '0.0737', '17.3', 218, NULL),
('BLK-PE-150-0.012-0-FL-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('BLK-PE-127-0.012-0-FL-R', 225, NULL, '0.0642', '14.4', 211, NULL),
('BLK-PE-191-0.016-0-NF-R', 225, NULL, '0.0600', '13.5', 212, NULL),
('GRE-PE-160-0.016-6-NF-R', 225, NULL, '0.0500', '11.2', 214, NULL),
('GRE-PE-190-0.025-0-NF-Q', 170, NULL, '0.0670', '11.3', 159, NULL),
('GRE-PE-170-0.025-0-NF-Q', 170, NULL, '0.0650', '11.0', 159, NULL),
('RED-PE-180-0.014-6-NF-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('GRY-PE-070-0.016-4-NF-R', 215, NULL, '0.0700', '15.0', 160, NULL),
('YEL-PE-82-0.015-0-NF-R', 225, NULL, '0.0800', '18.0', 160, NULL),
('CRE-PE-325-0.045-0-FL-O', 235, 155, '0.0877', '20.6', 214, '134.00'),
('CRE-PE-365-0.045-0-FL-O', 235, NULL, '0.1035', '24.3', 211, NULL),
('BLK-PE-178-0.030-0-NF-O', 235, NULL, '0.0640', '15.0', 220, NULL),
('BLK-PE-110-0.007-0-NF-R', 110, NULL, '0.0737', '8.11', 102, NULL),
('WHI-PE-070-0.016-4-NF-R', 215, NULL, '0.0700', '15.0', 160, NULL),
('WHI-PE-090-0.016-6-NF-R', 225, NULL, '0.0526', '11.8', 170, NULL),
('RED-PE-250-0.0470-NF-O', 235, 155, '0.0785', '18.4', 217, '137.00'),
('BLU-PE-251-0.047-0-NF-O', 235, 155, '0.1193', '28.0', 207, '127.00'),
('GRE-PE-170-0.040-0-NF-R', 235, 155, '0.0650', '15.2', 220, '140.00'),
('GRE-PE-112-0.024-0-NF-R', 235, NULL, '0.0568', '13.3', 222, NULL),
('BLK-PE-145-0.014-6-NF-O', 225, NULL, '0.0840', '18.9', 206, NULL),
('BLK-PE-110-0.010-0-NF-O', 190, NULL, '0.0740', '14.0', 176, NULL),
('BLU-PE-145-0.011-6-FL-R', 200, NULL, '0.0840', '16.8', 183, NULL),
('BRO-PE-180-0.040-0-NF-O', 235, NULL, '0.0530', '12.4', 223, NULL),
('BLK-PE-130-0.008-0-NF-R', 125, NULL, '0.0877', '10.9', 114, NULL),
('BLK-PE-160-0.008-NF-R', 125, NULL, '0.0500', '6.25', 119, NULL),
('BLK-PE-175-0.020-4-NF-R', 235, NULL, '0.0789', '18.5', 216, NULL),
('BLK-PE-130-0.022-0-NF-R', 235, NULL, '0.0877', '20.6', 214, NULL),
('GRE-PE-203-0.043-0-NF-O', 235, NULL, '0.0740', '17.3', 218, NULL),
('BLK-PE-110-0.020-0-NF-R', 235, NULL, '0.0737', '17.3', 218, NULL),
('BRO-PE-230-0.016-0-NF-R', 225, NULL, '0.1050', '23.6', 201, NULL),
('GRE-PE-110-0.024-0-NF-R', 225, NULL, '0.0737', '16.5', 208, NULL),
('BLK-PE-145-0.014-6-NF-R', 225, NULL, '0.0840', '18.9', 206, NULL),
('BLK-PE-178-0.010-6-0-FL-R', 190, NULL, '0.0640', '12.1', 178, NULL),
('SIL-PP-176-0.014-4-PF-O', 160, NULL, '0.0737', '11.7', 148, NULL),
('GRE-PE-385-0.031-0-FL-R', 235, NULL, '0.0544', '12.7', 222, NULL),
('GRE-PE-175-0.028-4-NF-R', 235, NULL, '0.0789', '18.5', 216, NULL),
('YEL-PE-125-0.016-0-NF-R', 225, NULL, '0.0789', '17.7', 207, NULL),
('GRE-PE-178-0.038-0-NF-R', 235, NULL, '0.0640', '15.0', 220, NULL),
('BLK-PE-310-0.047-0-NF-O', 235, NULL, '0.1298', '30.5', 204, NULL),
('SIL-PE-085-0.010-0-NF-R', 190, NULL, '0.0456', '8.66', 165, NULL),
('PIN-PE-1260-.014-0-FL-R', 225, NULL, '0.1158', '26.0', 199, NULL),
('BLU-PE-1260-.014-0-FL-R', 225, NULL, '0.1158', '26.0', 199, NULL),
('GRE-PE-1260-.014-0-FL-R', 225, NULL, '0.1158', '26.0', 199, NULL),
('YEL-PE-178-0.011-0-FL-R', 200, NULL, '0.0640', '12.8', 187, NULL),
('GRE-PE-1260-0.014-0-FL-R', 225, NULL, '0.1158', '26.0', 199, NULL),
('GRE-PE-110-0.020-0-NF-R', 235, NULL, '0.0740', '17.3', 218, NULL),
('BLU-PE-110-0.020-0-NF-R', 235, NULL, '0.0740', '17.3', 218, NULL),
('BLK-PE-1260-0.014-0-FL-R', 225, NULL, '0.1158', '26.0', 199, NULL),
('BLK-PE-125-0.010-6-FL-R', 190, NULL, '0.0789', '14.9', 175, NULL),
('BLK-PE-175-0.030-0-NF-R', 235, NULL, '0.0789', '18.5', 216, NULL),
('GRY-PE-146-0.013-4-PF-R', 190, NULL, '0.0779', '14.8', 175, NULL),
('GRE-PE-230-0.014-0-NF-R', 225, NULL, '0.1050', '23.6', 201, NULL),
('GRY-PE-156-0.011-6-FL-R', 200, NULL, '0.0695', '13.9', 186, NULL),
('CRE-PE-170-0.010-0-NF-R', 190, NULL, '0.0650', '12.3', 178, NULL),
('CRE-PE-305-0.010-0-NF-R', 190, NULL, '0.1439', '27.3', 163, NULL),
('RED-PE-265-0.043-0-NF-O', 235, NULL, '0.0702', '16.5', 219, NULL),
('GRE-PE-265-0.043-0-NF-O', 235, NULL, '0.0550', '12.9', 222, NULL),
('GRE-PE-203-0.043-0-NF-O', 235, NULL, '0.0550', '12.9', 222, NULL),
('GRE-PE-265-0.043-0-NF-O', 235, NULL, '0.0702', '16.5', 219, NULL),
('YEL-PE-170-0.035-0-NF-R', 235, NULL, '0.0650', '15.2', 220, NULL),
('WHI-PE-060-0.013-6-NF-R', 225, NULL, '0.0526', '11.8', 150, NULL),
('WHI-PE-1300-0.011-6-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('WHI-PE-1300-0.011-0-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('CRE-PE-1300-0.011-0-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('CRE-PE-1300-0.011-6-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('YEL-PE-1300-0.011-6-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('ORG-PE-1260-0.014-0-FL-R', 225, NULL, '0.1158', '26.0', 199, NULL),
('GRE-PE-610-0.016-4-PF-R', 215, NULL, '0.1439', '30.9', 184, NULL),
('ORG-PE-090-0.016-6-NF-R', 225, NULL, NULL, '-', 225, NULL),
('BLK-PE-52-0.007-0-NF-R', 110, NULL, '0.0695', '7.65', 102, NULL),
('BLK-PE-170-0.018-4-FL-R', 225, NULL, '0.0650', '14.6', 210, NULL),
('BLK-PE-175-0.023-4-NF-R', 235, NULL, '0.0789', '18.5', 216, NULL),
('YEL-PE-385-0.024-4-FL-R', 235, NULL, '0.0544', '12.7', 222, NULL),
('YEL-PE-285-0.024-0-FL-R', 235, NULL, '0.1000', '23.5', 212, NULL),
('BLU-PE-285-0.024-0-FL-R', 235, NULL, '0.1000', '23.5', 212, NULL),
('BLU-PE-385-0.024-4-FL-R', 235, NULL, '0.0544', '12.7', 222, NULL),
('GRE-PE-385-0.024-4-FL-R', 235, NULL, '0.0544', '12.7', 222, NULL),
('GRE-PE-285-0.024-0-FL-R', 235, NULL, '0.1000', '23.5', 212, NULL),
('YEL-PE-100-0.015-0-NF-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('GRE-PE-635-0.016-4-PF-R', 215, NULL, '0.1088', '23.3', 192, NULL),
('GRE-PE-090-0.016-0-FL-R', 225, NULL, NULL, '-', 170, NULL),
('YEL-PE-285-0.016-0-FL-R', 225, NULL, '0.1000', '22.5', 203, NULL),
('GRE-PE-432-0.040-4-NF-X', 155, NULL, '0.0900', '13.9', 141, NULL),
('BLK-PE-160-0.008-0-NF-R', 125, NULL, '0.0500', '6.25', 119, NULL),
('BLK-PE-153-0.016-0-NF-R', 225, NULL, '0.0874', '19.6', 205, NULL),
('YEL-PE-082-0.015-0-NF-R', 225, NULL, '0.0800', '18.0', 160, NULL),
('YEL-PE-182-0.015-0-NF-R', 225, NULL, '0.1050', '23.6', 201, NULL),
('YEL-PE-250-0.020-0-FL-R', 235, NULL, '0.0785', '18.4', 217, NULL),
('LGR-PE-250-0.020-0-FL-R', 235, NULL, '0.0785', '18.4', 217, NULL),
('PIN-PE-080-0.016-0-FL-R', 225, NULL, '0.0456', '10.2', 160, NULL),
('SIL-PE-315-0.018-0-FL-TR', 225, NULL, '0.1158', '26.0', 199, NULL),
('YEL-PE-384-0.031-0-FL-R', 235, NULL, '0.0568', '13.3', 222, NULL),
('BLU-PE-385-0.030-0-FL-R', 235, NULL, '0.0544', '12.7', 222, NULL),
('PUR-PE-110-0.024-0-NF-R', 235, NULL, '0.0740', '17.3', 218, NULL),
('BLK-PE-110-0.024-0-NF-R', 235, NULL, '0.0740', '17.3', 218, NULL),
('BLK-PE-185-0.030-0-NF-O', 235, NULL, '0.0910', '21.3', 214, NULL),
('BLK-PE-185-0.030-2-NF-O', 235, NULL, '0.0910', '21.3', 214, NULL),
('ORG-PE-225-0.015-0-FL-TR', 210, NULL, '0.0550', '11.5', 198, NULL),
('SIL-PE-315-0.018-0-FL-R', 225, NULL, '0.1158', '26.0', 199, NULL),
('BLK-PE-145-0.008-0-NF-R', 125, NULL, '0.0840', '10.5', 115, NULL),
('BLK-PE-060-0.010-0-NF-R', 190, NULL, NULL, '-', 150, NULL),
('GRY-PE-190-0.011-6-FL-R', 200, NULL, '0.0670', '13.4', 187, NULL),
('BLK-PE-175-0.014-6-FL-R', 225, NULL, '0.0789', '17.7', 207, NULL),
('BLK-PE-177-0.026-0-NF-R', 235, NULL, '0.0684', '16.0', 219, NULL),
('GRE-PE-203-0.043-0-NF-R', 235, 155, '0.0740', '17.3', 218, '137.61'),
('GRE-PE-178-0.038-0-NF-O', 235, NULL, '0.0640', '15.0', 220, NULL),
('PUR-PE-225-0.015-0-FL-TR', 210, NULL, '0.0526', '11.0', 199, NULL),
('BRO-PE-226-0.030-0-NF-R', 235, NULL, '0.0530', '12.4', 223, NULL),
('BLK-PE-165-0.010-6-NF-R', 190, NULL, '0.0737', '14.0', 176, NULL),
('BLK-PE-177-0.26-0-NF-R', 235, NULL, '0.0684', '16.0', 219, NULL),
('CRE-PE-230-0.014-0-NF-R', 225, NULL, '0.1050', '23.6', 201, NULL),
('RED-PE-650-0.016-4-NF-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('BLU-PE-650-0.016-4-NF-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('BLK-PE-052-0.007-0-NF-R', 110, NULL, '0.0700', '7.70', 102, NULL),
('YEL-PE-140-0.026-4-NF-R', 235, NULL, '0.0667', '15.6', 219, NULL),
('RED-PE-265-0.043.0-NF-O', 235, NULL, '0.0700', '16.4', 219, NULL),
('GRE-PE-265-0.043.0-NF-O', 235, NULL, '0.0700', '16.4', 219, NULL),
('GRE-PE-203-0.043.0-NF-O', 235, NULL, '0.0740', '17.3', 218, NULL),
('RED-PE-225-0.015-0-FL-T', 190, NULL, '0.0550', '10.4', 180, NULL),
('GRE-PE-225-0.015-0-FL-T', 190, NULL, '0.0550', '10.4', 180, NULL),
('GRE-PE-115-0.024-0-NF-R', 225, NULL, '0.0720', '16.2', 209, NULL),
('GRE-PE-385-0.031-0-FL--R', 235, NULL, '0.0544', '12.7', 222, NULL),
('BLK-PE-175-0.018-4-NF-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('BLK-PE-285-0.030-0-FL-R', 235, NULL, '0.1000', '23.5', 212, NULL),
('BLK-PE-230-0.030-0-NF-O', 235, NULL, '0.1050', '24.6', 210, NULL),
('BLK-PE-110-0.008-0-NF-R', 125, NULL, '0.0740', '9.25', 116, NULL),
('LBL-PE-060-0.013-6-NF-R', 225, NULL, NULL, '-', 225, NULL),
('RED-PE-060-0.013-6-NF-R', 225, NULL, NULL, '-', 225, NULL),
('GRY-PE-060-0.013-6-NF-R', 225, NULL, NULL, '-', 225, NULL),
('GRE-PE-060-0.013-6-NF-O', 225, NULL, NULL, '-', 225, NULL),
('RED-PE-225-0.015-0-FL-TR', 210, NULL, '0.0526', '11.0', 199, NULL),
('BLU-PE-178-0.022-0-NF-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('BLK-PE-165-0.016-0-FL-R', 225, NULL, '0.0737', '16.5', 208, NULL),
('WHI-PE-140-0.018-6-NF-R', 225, NULL, '0.0667', '15.0', 210, NULL),
('GLD-PE-205-0.016-0-FL-R', 225, NULL, '0.0649', '14.6', 210, NULL),
('YEL-PE-145-0.014-0-NF-R', 225, NULL, '0.0840', '18.9', 206, NULL),
('GRE-PE-176-0.014-0-NF-R', 225, NULL, '0.0737', '16.5', 208, NULL),
('RED-PE-176-0.014-0-NF-R', 225, NULL, '0.0737', '16.5', 208, NULL),
('BLU-PE-176-0.014-0-NF-R', 225, NULL, '0.0737', '16.5', 208, NULL),
('YEL-PE-178-0.013-6-NF-R', 225, NULL, '0.0632', '14.2', 211, NULL),
('RED-PE-178-0.013-6-NF-R', 225, NULL, '0.0632', '14.2', 211, NULL),
('BLU-PE-178-0.013-6-NF-R', 225, NULL, '0.0632', '14.2', 211, NULL),
('GRE-PE-178-0.013-6-NF-R', 225, NULL, '0.0632', '14.2', 211, NULL),
('WHI-PE-310-0.047-0-NF-O', 235, NULL, '0.1298', '30.5', 204, NULL),
('WHI-PE-250-0.047-0-NF-O', 235, NULL, '0.0785', '18.4', 217, NULL),
('RED-PE-230-0.043.0-NF-O', 235, NULL, '0.1050', '24.6', 210, NULL),
('RED-PE-210-0.031-0-NF-R', 235, NULL, '0.0500', '11.7', 223, NULL),
('WHI-PE-145-0.014-0-NF-R', 225, NULL, '0.0840', '18.9', 206, NULL),
('RED-PE-170-0.035-0-NF-O', 235, NULL, '0.0650', '15.2', 220, NULL),
('PUR-PE-130-0.012-0-FL-R', 225, NULL, '0.0880', '19.8', 205, NULL),
('BLU-PE-100-0.015-0-NF-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('WHI-PE-145-0.018-6-NF-R', 225, NULL, '0.0842', '18.9', 206, NULL),
('RED-PE-230-0.022-0-FL-R', 235, NULL, '0.1050', '24.6', 210, NULL),
('BLU-PP-067-0.013-0-NF-O', 160, NULL, '0.0596', '9.54', 150, NULL),
('PIN-PP-067-0.013-0-NF-O', 160, NULL, '0.0596', '9.54', 150, NULL),
('GRE-PP-067-0.013-0-NF-O', 160, NULL, '0.0596', '9.54', 150, NULL),
('WHI-PE-140-0.011-6-FL-R', 200, NULL, '0.0667', '13.3', 187, NULL),
('YEL-PE-070-0.016-4-NF-R', 215, NULL, '0.0700', '15.0', 200, NULL),
('BLK-PE-140-0.011-6-FL-R', 200, NULL, '0.0667', '13.3', 187, NULL),
('BLU-PE-160-0.016-6-NF-R', 225, NULL, '0.0500', '11.2', 214, NULL),
('RED-PE-160-0.016-6-NF-R', 225, NULL, '0.0500', '11.2', 214, NULL),
('GRE-PE-178-0.025-0-NF-R', 235, NULL, '0.0640', '15.0', 220, NULL),
('GRY-PE-172-0.022-4-NF-R', 235, NULL, '0.0947', '22.2', 213, NULL),
('BLK-PE-172-0.022-4-NF-R', 235, NULL, '0.0947', '22.2', 213, NULL),
('RED-PE-225-0.022-0-NF-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('BLK-PE-255-0.030-0-FL-R', 235, NULL, '0.1075', '25.2', 210, NULL),
('BLK-PE-170-0.040-0-NF-O', 235, NULL, '0.0456', '10.7', 224, NULL),
('BLK-PE-140-0.040-0-NF-O', 235, NULL, '0.0667', '15.6', 219, NULL),
('BEI-PE-650-0.016-4-PF-R', 215, NULL, '0.0877', '18.8', 196, NULL),
('RED-PE-225-0.022-0-FL-R', 235, NULL, '0.0526', '12.3', 223, NULL),
('DGR-PE-650-0.016-4-PF-R', 215, NULL, '0.0877', '18.8', 196, NULL),
('RED-PE-145-0.014-0-NF-R', 225, NULL, '0.0840', '18.9', 206, NULL),
('BLU-PE-155-0.014-0-NF-R', 225, NULL, '0.0754', '16.9', 208, NULL),
('GRY-PE-145-0.011-0-FL-R', 200, NULL, '0.0840', '16.8', 183, NULL),
('GRE-PE-255-0.031-0-FL-R', 235, NULL, '0.1075', '25.2', 210, NULL),
('GRE-PE-220-0.052-0-NF-O', 235, NULL, '0.0740', '17.3', 218, NULL),
('GRE-PE-635-0.018-4-FL-R', 225, NULL, '0.1088', '24.4', 201, NULL),
('WHI-PE-175-0.028-4-NF-R', 235, NULL, '0.0789', '18.5', 216, NULL),
('GRE-PE-230-0.032-0-NF-Q', 170, NULL, '0.1050', '17.8', 152, NULL),
('WHI-PE-1300-0.010-0-NF-R', 190, NULL, '0.0877', '16.6', 173, NULL),
('RED-PE-1200-0.008-0-FL-H', 70, NULL, NULL, '-', 70, NULL),
('BLU-PE-1200-0.008-0-FL-H', 70, NULL, NULL, '-', 70, NULL),
('GRE-PE-1200-0.008-0-FL-H', 70, NULL, NULL, '-', 70, NULL),
('RED-PE-090-0.016-6-NF-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('WHI-PE-1300-0.016-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('BLK-PE-1200-0.007-0-PF-H', 70, NULL, NULL, '-', 70, NULL),
('GLD-PE-245-0.016-0-FL-R', 225, NULL, '0.0544', '12.2', 213, NULL),
('GRE-PE-225-0.015-0-PF-T', 190, NULL, '0.0550', '10.4', 180, NULL),
('RED-PE-225-0.015-0-PF-T', 190, NULL, '0.0550', '10.4', 180, NULL),
('BLK-PE-140-0.010-2-NF-R', 135, NULL, '0.0667', '9.00', 126, NULL),
('ORG-PE-1300-0.010-0-NF-R', 190, NULL, '0.0877', '16.6', 173, NULL),
('PUR-PE-1300-0.010-0-NF-R', 190, NULL, '0.0877', '16.6', 173, NULL),
('GRE-PE-1300-0.010-0-NF-R', 190, NULL, '0.0877', '16.6', 173, NULL),
('PIN-PE-1300-0.010-0-NF-R', 190, NULL, '0.0877', '16.6', 173, NULL),
('PIN-PE-1300-0.016-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('ORG-PE-1300-0.016-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('PUR-PE-1300-0.016-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('GRE-PE-1300-0.016-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('SIL-PE-1200-0.008-0-FL-H', 70, NULL, NULL, '-', 70, NULL),
('BLU-PE-178-0.015-6-NF-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('GRE-PE-178-0.015-6-NF-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('BLU-PE-179-0.014-6-NF-R', 225, NULL, '0.0579', '13.0', 212, NULL),
('RED-PE-178-0.015-6-NF-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('PUR-PE-178-0.015-6-NF-R', 225, NULL, '0.0640', '14.4', 211, NULL),
('BLK-PE-240-0.028-4-NF-R', 235, NULL, '0.0737', '17.3', 218, NULL),
('RED-PE-285-0.024-0-FL-R', 235, NULL, '0.1000', '23.5', 212, NULL),
('RED-PE-179-0.014-6-NF-R', 225, NULL, '0.0579', '13.0', 212, NULL),
('GRY-PE-180-0.014-6-NF-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('GRE-PE-180-0.016-4-NF-R', 215, NULL, '0.0526', '11.3', 204, NULL),
('BRO-PE-180-0.020-0-NF-R', 235, NULL, '0.0526', '12.3', 223, NULL),
('BLK-PE-125-0.010-0-NF-R', 190, NULL, '0.0789', '14.9', 175, NULL),
('RED-PE-170-0.028-0-NF-R', 235, NULL, '0.0650', '15.2', 220, NULL),
('SIL-PE-125-0.010-0-NF-R', 190, NULL, '0.0789', '14.9', 175, NULL),
('RED-PE-250-0.047-0-NF-O', 235, NULL, '0.0785', '18.4', 217, NULL),
('YEL-PE-180-0.016-4-NF-R', 215, NULL, '0.0526', '11.3', 204, NULL),
('RED-PE-175-0.016-4-NF-R', 215, NULL, '0.0789', '16.9', 198, NULL),
('YEL-PE-325/385-0.031-0-FL-R', 235, NULL, '0.0700', '16.4', 219, NULL),
('SIL-PE-145/178-0.016-4-FL-R', 215, NULL, '0.0348', '7.48', 208, NULL),
('GRY-PE-1300-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('YEL-PE-1300-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('BEI-PE-1200-0.010-0-PF-H', 70, NULL, NULL, NULL, 70, NULL),
('CRE-PE-305/170-0.010-0-NF-R', 190, NULL, NULL, '-', 190, NULL),
('BEI-PP-055-0.015-5-NF-T', 160, NULL, '0.0600', '9.60', 150, NULL),
('ORG-PE-060-0.013-6-NF-R', 225, NULL, '0.0526', '11.8', 150, NULL),
('GRE-PE-060-0.013-6-NF-R', 225, NULL, '0.0526', '11.8', 150, NULL),
('WHI-PE-060-0.013-6-NF-R', 225, NULL, '0.0526', '11.8', 150, NULL),
('BLU-PPHD-240-0.023-0-FL-R', 160, NULL, '0.0737', '11.7', 148, NULL),
('GRE-PP-087-0.013-0-NF-O', 160, NULL, '0.0537', '8.59', 135, NULL),
('GRE-PE-180-0.014-6-NF-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('GRE-PE-1300-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('RED-PE-1300-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('BLU-PP-087-0.013-0-NF-O', 160, NULL, '0.0537', '8.59', 135, NULL),
('BLU-PE-080-0.016-0-FL-R', 225, NULL, '0.0456', '10.2', 160, NULL),
('GRE-PE-080-0.016-0-FL-R', 225, NULL, '0.0456', '10.2', 160, NULL),
('BLK-PE-1200-0.010-0-PF-H', 70, NULL, NULL, NULL, 70, NULL),
('BLK-PE-127-0.028-4-NF-R', 235, NULL, '0.0642', '15.0', 220, NULL),
('BLK-PE-210-0.028-0-NF-R', 235, NULL, '0.0500', '11.7', 223, NULL),
('PIN-PP-087-0.013-0-NF-O', 160, NULL, '0.0537', '8.59', 135, NULL),
('WHI-PP-087-0.013-0-NF-O', 160, NULL, '0.0596', '9.54', 135, NULL),
('WHI-PP-080-0.016-0-NF-O', 160, NULL, '0.0596', '9.54', 135, NULL),
('GRE-PE-250-0.040-0-NF-O', 235, 155, '0.0740', '17.3', 218, NULL),
('RED-PE-180-0.040-0-NF-R', 235, NULL, '0.0700', '16.4', 219, NULL),
('BLK-PE-225-0.028-4-NF-R', 235, NULL, '0.0642', '15.0', 220, NULL),
('RED-PE-220-0.055-0-NF-O', 235, NULL, '0.0740', '17.3', 218, NULL),
('GRE-PE-145-0.011-0-FL-O', 225, NULL, '0.1000', '22.5', 106, NULL),
('ORG-PE-250-0.020-0-FL-R', 235, NULL, '0.0785', '18.4', 217, NULL),
('RED-PE-250-0.020-0-FL-R', 235, NULL, '0.0785', '18.4', 217, NULL),
('BLU-PE-250-0.020-0-FL-R', 235, NULL, '0.0785', '18.4', 217, NULL),
('BLU-PE-181-0.014-6-NF-R', 225, NULL, '0.0526', '11.8', 213, NULL),
('CRE-PPLD-335-0.040-0-FL-O', 150, 100, NULL, NULL, 200, '100.00'),
('BLU-PE-150-0.016-0-FL-R', 225, NULL, '0.0840', '18.9', 206, NULL),
('GRE-PE-150-0.016-0-FL-R', 225, NULL, '0.0840', '18.9', 206, NULL),
('PIN-PE-150-0.016-0-FL-R', 225, NULL, '0.0840', '18.9', 206, NULL),
('BLK-PE-145-0.008-6-NF-R', 125, NULL, '0.0840', '10.5', 115, NULL),
('BEI-PP-080-0.014-6-NF-O', 160, NULL, '0.0537', '8.59', 135, NULL),
('WHI-PE-100-0.015-0-NF-R', 225, NULL, '0.0840', '18.9', 206, NULL),
('GRY-PE-180-0.016-0-PF-R', 190, NULL, '0.1000', '19.0', 171, NULL),
('BLK-PE-180-0.016-0-PF-R', 225, NULL, '0.0600', '13.5', 212, NULL),
('BLU-PE-305-0.070-0-NF-O', NULL, 160, '0.1439', '23.0', NULL, '136.96'),
('BLU-PE-254-0.070-0-NF-O', NULL, 160, '0.1439', '23.0', NULL, '136.96'),
('WHI-PP-50-0.014-2.5-NF-O', 160, NULL, NULL, NULL, 120, NULL),
('YEL-PP-50-0.014-2.5-NF-O', 160, NULL, NULL, NULL, 120, NULL),
('YEL-PE-1300-0.011-0-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('YEL-PE-1300-0.013-0-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('PIN-PE-1300-0.011-0-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('BLU-PE-1300-0.011-0-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('BLU-PE-1300-0.013-0-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('GRE-PE-1300-0.013-0-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('RED-PE-1300-0.013-0-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('RED-PE-1260-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('BLK-PE-1300-0.013-0-NF-R', 200, NULL, '0.0877', '17.5', 182, NULL),
('Red-PE-171-0.040-0-NF-O', 235, NULL, '0.0456', '10.7', 224, NULL),
('RED-PE-250-0.055-0-NF-O', 235, NULL, '0.0740', '17.3', 218, NULL),
('BEI-PE-650-0.020-4-PF-R', 215, NULL, '0.0877', '18.8', 196, NULL),
('DGR-PE-650-0.014-4-PF-R', 215, NULL, '0.0877', '18.8', 196, NULL),
('BLK-PE-300-0.028-0-FL-R', 235, NULL, '0.0500', '11.7', 223, NULL),
('BLK-PE-330-0.028-0-FL-R', 235, NULL, '0.0500', '11.7', 223, NULL),
('YEL-PP-385-0.040-0-NF-O', 150, NULL, '0.0544', '12.7', 222, NULL),
('RED-PE-230-0.011-0-NF-R', 200, NULL, '0.1050', '23.6', 201, NULL),
('RED-PE-175-0.011-0-NF-R', 200, NULL, '0.0789', '15.7', 184, NULL),
('RED-PE-230/175-0.011-0-NF-R', 200, NULL, '0.1050', '23.6', 201, NULL),
('RED-PE-175-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('RED-PE-1260/175-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('BLK-PE-1260-0.022-0-NF-R', 225, NULL, '0.1158', '26.0', 199, NULL),
('LBL-PE-1260/175-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('LBL-PE-230-0.011-0-NF-R', 200, NULL, '0.1050', '23.6', 201, NULL),
('GRE-PE-230-0.011-0-NF-R', 200, NULL, '0.1050', '23.6', 201, NULL),
('GRE-PE-610-0.016-4-FL-R', 215, NULL, '0.1439', '30.9', 184, NULL),
('GRE-PE-178-0.016-0-FL-R', 215, NULL, '0.0640', '13.7', 201, NULL),
('LBL-PE-1260-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('LBL-PE-175-0.014-0-FL-R', 225, NULL, '0.0877', '19.7', 205, NULL),
('GRE-PE-230/175-0.011-0-NF-R', 200, NULL, '0.1050', '23.6', 201, NULL),
('GRE-PE-1260/175-0.014-0-FL-R', 225, NULL, '0.1158', '26.0', 199, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `parts`
--

DROP TABLE IF EXISTS `parts`;
CREATE TABLE IF NOT EXISTS `parts` (
  `PartID` int(11) NOT NULL AUTO_INCREMENT,
  `PartNumber` varchar(25) DEFAULT NULL,
  `swo` int(25) DEFAULT NULL,
  PRIMARY KEY (`PartID`)
) ENGINE=MyISAM AUTO_INCREMENT=205122 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parts`
--

INSERT INTO `parts` (`PartID`, `PartNumber`, `swo`) VALUES
(6, 'CRE-PE-325-0.045-0-FL-O', 123),
(5, 'RED-PE-220-0.055-0-NF-O', 345),
(7, 'CRE-PE-365-0.045-0-FL-O', 678),
(8, 'YEL-PE-325-0.031-0-FL-R', 910),
(9, 'YEL-PE-385-0.031-0-FL-R', 1011),
(205121, 'BLK-PE-160-0.040-0-NF-O', 333),
(205116, 'RED-PE-220-0.055-0-NF-P', 122),
(205115, 'CRE-PE-375-0.045-0-FL-O', 12);

-- --------------------------------------------------------

--
-- Table structure for table `prodoee`
--

DROP TABLE IF EXISTS `prodoee`;
CREATE TABLE IF NOT EXISTS `prodoee` (
  `Id` varchar(60) NOT NULL,
  `SOnumber` varchar(25) NOT NULL,
  `SWO` float NOT NULL,
  `Reject` float NOT NULL,
  `Output` float NOT NULL,
  `aTime` float NOT NULL,
  `pdTime` float NOT NULL,
  `dTime` float NOT NULL,
  `oTime` float NOT NULL,
  `irRate` float NOT NULL,
  `aRate` float NOT NULL,
  `pRate` float NOT NULL,
  `qRate` float NOT NULL,
  `Oee` float NOT NULL,
  `reportDate` date NOT NULL,
  `insertDate` date NOT NULL,
  `Shift` varchar(12) NOT NULL,
  `Supervisor` varchar(25) NOT NULL,
  `Technician` varchar(25) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `prodoee`
--

INSERT INTO `prodoee` (`Id`, `SOnumber`, `SWO`, `Reject`, `Output`, `aTime`, `pdTime`, `dTime`, `oTime`, `irRate`, `aRate`, `pRate`, `qRate`, `Oee`, `reportDate`, `insertDate`, `Shift`, `Supervisor`, `Technician`) VALUES
('GRE-PE-610-0.016-4-FL-R-2018-06-20-Day', 'asd', 215, 3, 123, 300, 10, 10, 280, 358.33, 96.55, 12.26, 97.56, 11.55, '2018-06-20', '2018-06-25', 'Day', 'Kamal', 'Sunil'),
('YEL-PE-1300-0.011-0-NF-R-2018-06-20-Day', 'qwe', 200, 2, 142, 420, 10, 10, 400, 333.33, 97.56, 10.65, 98.59, 10.24, '2018-06-20', '2018-06-25', 'Day', 'Kamal', 'Sunil');

-- --------------------------------------------------------

--
-- Table structure for table `production`
--

DROP TABLE IF EXISTS `production`;
CREATE TABLE IF NOT EXISTS `production` (
  `ProdId` varchar(255) NOT NULL,
  `SO_Num` varchar(15) NOT NULL,
  `Customer` varchar(15) NOT NULL,
  `InsertDate` date NOT NULL,
  `ReportDate` date NOT NULL,
  `Shift` varchar(10) NOT NULL,
  `PartNumber` varchar(30) NOT NULL,
  `prod_qty` float NOT NULL DEFAULT '0',
  `pack_qty` float NOT NULL DEFAULT '0',
  `CartNew` varchar(15) NOT NULL,
  `CartUsed` varchar(15) NOT NULL,
  `Bags` varchar(15) NOT NULL,
  `Supervisor` varchar(20) NOT NULL,
  `Technician` varchar(20) NOT NULL,
  PRIMARY KEY (`ProdId`)
) ENGINE=MyISAM AUTO_INCREMENT=184 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `production`
--

INSERT INTO `production` (`ProdId`, `SO_Num`, `Customer`, `InsertDate`, `ReportDate`, `Shift`, `PartNumber`, `prod_qty`, `pack_qty`, `CartNew`, `CartUsed`, `Bags`, `Supervisor`, `Technician`) VALUES
('CRE-PE-325-0.045-0-FL-O-2018-06-07-Night', 'qqwe', 'qqweq', '2018-06-25', '2018-06-07', 'Night', 'CRE-PE-325-0.045-0-FL-O', 142, 362, '', '', '', 'qewq', 'qweqe'),
('YEL-PE-1300-0.011-0-NF-R-2018-06-20-Day', 'qwe', 'qwe', '2018-06-25', '2018-06-20', 'Day', 'YEL-PE-1300-0.011-0-NF-R', 142, 392, '', '', '', 'Kamal', 'Sunil'),
('GRE-PE-610-0.016-4-FL-R-2018-06-20-Day', 'asd', 'asdas', '2018-06-25', '2018-06-20', 'Day', 'GRE-PE-610-0.016-4-FL-R', 123, 321, '', '', '', 'Kamal', 'Sunil');

-- --------------------------------------------------------

--
-- Table structure for table `productiondowntime`
--

DROP TABLE IF EXISTS `productiondowntime`;
CREATE TABLE IF NOT EXISTS `productiondowntime` (
  `Id` int(50) NOT NULL AUTO_INCREMENT,
  `Status` varchar(20) NOT NULL,
  `Attribute` varchar(10) NOT NULL,
  `TimeFrom` varchar(10) NOT NULL,
  `TimeTo` varchar(10) NOT NULL,
  `TotalTime` varchar(10) NOT NULL,
  `Reason` varchar(30) NOT NULL,
  `Comment` varchar(255) DEFAULT NULL,
  `InsertDate` date NOT NULL,
  `ReportDate` date NOT NULL,
  `Shift` varchar(20) NOT NULL,
  `Supervisor` varchar(20) NOT NULL,
  `Technician` varchar(20) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `productiondowntime`
--

INSERT INTO `productiondowntime` (`Id`, `Status`, `Attribute`, `TimeFrom`, `TimeTo`, `TotalTime`, `Reason`, `Comment`, `InsertDate`, `ReportDate`, `Shift`, `Supervisor`, `Technician`) VALUES
(165, 'Planned', 'BLK-PE-160', '7.30', '7.45', '15', 'Item Changing & Cleaning ', 'qweqwe', '2018-06-25', '2018-06-14', 'Night', 'Kamal', 'Sumal'),
(166, 'Unplanned', 'BLK-PE-160', '12.30', '12.42', '12', 'Process Issues', 'qweqwe', '2018-06-25', '2018-06-14', 'Night', 'Kamal', 'Sumal'),
(212, 'Planned', 'GRE-PE-610', '7.30', '7.40', '10', 'Heating', '', '2018-06-25', '2018-06-20', 'Day', 'Kamal', 'Sunil'),
(213, 'Unplanned', 'GRE-PE-610', '7.30', '7.40', '10', 'Machine Line Technical Errors', '', '2018-06-25', '2018-06-20', 'Day', 'Kamal', 'Sunil'),
(214, 'Planned', 'YEL-PE-130', '7.30', '7.40', '10', 'Winder Breakdown', '', '2018-06-25', '2018-06-20', 'Day', 'Kamal', 'Sunil'),
(215, 'Unplanned', 'YEL-PE-130', '7.30', '7.40', '10', 'Power Cut/Water Cut', '', '2018-06-25', '2018-06-20', 'Day', 'Kamal', 'Sunil');

-- --------------------------------------------------------

--
-- Table structure for table `productionmachinetime`
--

DROP TABLE IF EXISTS `productionmachinetime`;
CREATE TABLE IF NOT EXISTS `productionmachinetime` (
  `Id` varchar(50) NOT NULL,
  `PartNumber` varchar(20) NOT NULL,
  `Shift` varchar(10) NOT NULL,
  `InsertDate` date NOT NULL,
  `ReportDate` date NOT NULL,
  `TimeFrom` varchar(10) NOT NULL,
  `TimeTo` varchar(10) NOT NULL,
  `TotalTime` varchar(10) NOT NULL,
  `Supervisor` varchar(20) NOT NULL,
  `Technician` varchar(20) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `productionmachinetime`
--

INSERT INTO `productionmachinetime` (`Id`, `PartNumber`, `Shift`, `InsertDate`, `ReportDate`, `TimeFrom`, `TimeTo`, `TotalTime`, `Supervisor`, `Technician`) VALUES
('CRE-PE-325-0.045-0-FL-O-2018-06-14-Day', 'CRE-PE-325-0.045-0-F', '', '2018-06-25', '2018-06-14', '7.30', '12.30', '300', 'Kamal', 'Nimal'),
('YEL-PE-1300-0.011-0-NF-R-2018-06-20-Day', 'YEL-PE-1300-0.011-0-', 'Day', '2018-06-25', '2018-06-20', '12.30', '19.30', '420', 'Kamal', 'Sunil'),
('BLK-PE-160-0.040-0-NF-O-2018-06-14-Night', 'BLK-PE-160-0.040-0-N', 'Night', '2018-06-25', '2018-06-14', '7.30', '19.30', '720', 'Kamal', 'Sumal'),
('GRE-PE-610-0.016-4-FL-R-2018-06-20-Day', 'GRE-PE-610-0.016-4-F', 'Day', '2018-06-25', '2018-06-20', '7.30', '12.30', '300', 'Kamal', 'Sunil');

-- --------------------------------------------------------

--
-- Table structure for table `reject`
--

DROP TABLE IF EXISTS `reject`;
CREATE TABLE IF NOT EXISTS `reject` (
  `Id` varchar(255) NOT NULL,
  `InsertDate` date NOT NULL,
  `ReportDate` date NOT NULL,
  `Shift` varchar(10) NOT NULL,
  `Base` varchar(30) NOT NULL,
  `PartNumber` varchar(30) NOT NULL,
  `Value` float NOT NULL,
  `Supervisor` varchar(20) NOT NULL,
  `Technician` varchar(20) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reject`
--

INSERT INTO `reject` (`Id`, `InsertDate`, `ReportDate`, `Shift`, `Base`, `PartNumber`, `Value`, `Supervisor`, `Technician`) VALUES
('BLK-PE-160-0.040-0-NF-O-Colour Variation-Night', '2018-06-25', '2018-06-14', 'Night', 'Colour Variation', 'BLK-PE-160-0.040-0-NF-O', 1, 'Kamal', 'Sumal'),
('BLK-PE-160-0.040-0-NF-O-Line Setup-Night', '2018-06-25', '2018-06-14', 'Night', 'Line Setup', 'BLK-PE-160-0.040-0-NF-O', 2, 'Kamal', 'Sumal'),
('BLK-PE-160-0.040-0-NF-O-Winder Wastage-Night', '2018-06-25', '2018-06-14', 'Night', 'Winder Wastage', 'BLK-PE-160-0.040-0-NF-O', 3, 'Kamal', 'Sumal'),
('GRE-PE-610-0.016-4-FL-R-Material Quality-Day', '2018-06-25', '2018-06-20', 'Day', 'Material Quality', 'GRE-PE-610-0.016-4-FL-R', 1, 'Kamal', 'Sunil'),
('GRE-PE-610-0.016-4-FL-R-Wrapper Wastage-Day', '2018-06-25', '2018-06-20', 'Day', 'Wrapper Wastage', 'GRE-PE-610-0.016-4-FL-R', 2, 'Kamal', 'Sunil'),
('YEL-PE-1300-0.011-0-NF-R-Material Quality-Day', '2018-06-25', '2018-06-20', 'Day', 'Material Quality', 'YEL-PE-1300-0.011-0-NF-R', 2, 'Kamal', 'Sunil');

-- --------------------------------------------------------

--
-- Table structure for table `standardwastages`
--

DROP TABLE IF EXISTS `standardwastages`;
CREATE TABLE IF NOT EXISTS `standardwastages` (
  `Id` int(10) NOT NULL AUTO_INCREMENT,
  `CutLength` varchar(10) NOT NULL,
  `Wastage` float NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `standardwastages`
--

INSERT INTO `standardwastages` (`Id`, `CutLength`, `Wastage`) VALUES
(1, '325', 8.77),
(2, '220', 7.37),
(6, '385', 5.44),
(5, '365', 10.35),
(7, '160', 5);

-- --------------------------------------------------------

--
-- Table structure for table `wastage_calc`
--

DROP TABLE IF EXISTS `wastage_calc`;
CREATE TABLE IF NOT EXISTS `wastage_calc` (
  `A` varchar(10) DEFAULT NULL,
  `B` varchar(36) DEFAULT NULL,
  `C` decimal(9,2) DEFAULT NULL,
  `D` varchar(3) DEFAULT NULL,
  `E` varchar(10) DEFAULT NULL,
  `F` decimal(8,5) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `wastage_calc`
--

INSERT INTO `wastage_calc` (`A`, `B`, `C`, `D`, `E`, `F`) VALUES
(NULL, 'cutter wastage Calculator', NULL, NULL, NULL, NULL),
(NULL, 'Total length of a hank', '2850.00', 'mm', NULL, '131.44715'),
(NULL, 'what has toreduce after winding', '100.00', 'mm', NULL, '85.44715'),
(NULL, 'Total usable hank length', '2750.00', 'mm', NULL, '72.44715'),
(NULL, 'Cut Length', '1260.00', 'mm', NULL, NULL),
(NULL, 'No. of pieces from a hank', '2.18', 'Nos', NULL, NULL),
(NULL, 'How to divide', '1.00', '1', NULL, NULL),
(NULL, 'Byproduct', '230.00', 'mm', NULL, NULL),
(NULL, 'for Balancing', '30.00', 'mm', NULL, NULL),
(NULL, 'Length of byproduct that can be used', '200.00', NULL, NULL, NULL),
(NULL, 'Wastage %', '11.58', '%', NULL, NULL),
(NULL, NULL, '1111111.00', NULL, NULL, NULL),
(NULL, '*Yellow colums you have to fill', NULL, NULL, NULL, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
