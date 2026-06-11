This code represents the backend of a Dental Clinic Management Sofware

It handles Patient Records. Each Patient has a mouth, the mouth has teeth and each tooth has faces. Each teeth can be in different states and could have one or more practices associated to it through history.
When a patient is diagnosed with a disease, if he accepts the proposed practice to cure that disease, that practice is planned. Once the practice is executed, it is added to the practice history of that patient. The practice can span one or more faces of a single tooth, or can span to several whole teeth.
A tooth, AKA piece, is designated by a code, following ISO 3950 notation
A practice executed on a tooth or teeth affect the status of them. As an example, an Extraction practice changes the tooth status to Extracted. 
Status can be forced by the practitioner, and may not be associated with a practice. e.g.: Missing tooth extracted previously, by a different practitioner. This procedure of forcing each tooth status to the current status is carried out the first time the patient visits the practitioner.

Tooth satuses are encoded as strings

  Encoding is as follows:
 
    {piece number ISO 3950 notation}{Stat}[letters faces affected]
 
    Stat is a one letter code:
 
    *   Healthy - H
    *   Removed - X
    *   Filling - F
    *   Bridge  - B
    *   Crown   - C
    *   Implant - I
    *   Root C. - R
 
    Tooth faces names are written with the first letter (See enum ToothFaceName)
 
   Eg.:
 
  Tooth# 48 with Filling in oclusal: 48FO
  Tooth# 48 with Filling in vestibular and messial: 48FVM
  Tooth# 17 removed: 17X
  Tooth# 11 Healthy: 11H
  Tooth# 34 Implant: 34I
  Tooth# 41-44 Bridge: 41BS,42BI,43BI,44BE
  where BS means Bridge Start, BI means Bridge Intermediate and BE means Bridge End
 