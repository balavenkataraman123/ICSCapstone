import cv2
trackimage = cv2.imread("Racetrack1.png")

pixels_per_meter = 50
meters_per_image = 48
overlap_per_image = 24

ntimage = cv2.resize(trackimage, (4000, 1545*2),interpolation = cv2.INTER_LINEAR)
cv2.imwrite("RaceTrack1.png", ntimage)
