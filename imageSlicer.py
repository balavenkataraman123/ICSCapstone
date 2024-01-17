import cv2
trackimage = cv2.imread("Racetrack1.png")

pixels_per_meter = 50
meters_per_image = 48
overlap_per_image = 24

print(trackimage.shape)


