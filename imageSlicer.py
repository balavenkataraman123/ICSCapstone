import cv2
for i in range(1,4):
    trackimage = cv2.imread("Racetrack" + str(i) + ".png")

    pixels_per_meter = 50
    meters_per_image = 48
    overlap_per_image = 24

    #ntimage = cv2.resize(trackimage, (trackimage.shape[1] * 2, trackimage.shape[0] * 2),interpolation = cv2.INTER_LINEAR)
    cv2.imwrite("RaceTrack" + str(i) + ".png", trackimage)
