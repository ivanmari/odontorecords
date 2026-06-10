This is a Dental Clinic Management application.
The objective is to register practices executed by practitioners on patients.
Patients are represented as a class with personal data and also an odontograme. The odontograme represents the health status of a mouth. The mouth have teeth and the teeth have faces.
Practices can be associated with one or more faces on a tooth or could span to multiple teeth, like in a Bridge practice, where the Bridge affects one or more teeth.
When the patient visits the clinic for some proble, the practitioner can diagnose a disease on a tooth or several teeth. The patient decides if he accepts the treatement and then the practitioner plans the practice. The odontograme is used to register this planned practice. Each practice has a code and each tooth is determined by a pair of integers, following the ISO 3950 standard. The color used to indicate this practis as planned is red. When the practice is executed, the color changes to red.
The first time a patient visits the clinic, the practitioner registers the status of each tooth in the odontograme. The status of a tooth is the result of one or more practices applied to it. As an example, consider a tooth whose mesial face was applied the Fill practice, then it suffered a severe Cavity disease so that it cannot be treated with the Fill practice, but need to apply the Extract practice. In this case the status is Removed. That tooth is not present anymore.
If that previous tooth is later replaced by an Implant, then the Implant Practice is applied to it and its status is Implant, not Removed.
A practice can be charged directly to a patient or to a social security provider. The social security providers use a practice table with codes for each practice and the amount they will pay to the practitioner.
Sometimes the practice is expensive, so the patient pays it in several installments. This is represented in Bookkeeping

The format of the status is register as follows:

   {piece number ISO 3950 notation}{Stat}[letters faces affected]

   Stat is a one letter code:

   Healthy - H

   Removed - X

   Filling - F

   Bridge  - B

   Crown   - C

   Implant - I

   Root C. - R

   Tooth faces names are written with the first letter (See enum ToothFaceName)

  Eg.:

 Tooth# 48 with Filling in oclusal: 48FO

 Tooth# 48 with Filling in vestibular and messial: 48FVM

 Tooth# 17 removed: 17X

 Tooth# 11 Healthy: 11H

 Tooth# 34 Implant: 34I

 Tooth# 41-44 Bridge: 41BS,42BI,43BI,44BE
 where BS means Bridge Start, BI Bridge Intermediate and BE Bridge End