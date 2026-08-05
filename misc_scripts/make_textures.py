from PIL import Image, ImageDraw, ImageFilter

for frame in range(1, 5):
    # 128x128 canvas, transparent background
    size = 128
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # The tear gets wider and more violent with each frame
    w = 8 * frame 
    
    # Coordinates for a jagged tear shape
    points = [
        (64, 16),                 # Top point
        (64 + w, 40),             # Top right bulge
        (64 + (w*0.5), 64),       # Middle right indent
        (64 + w, 88),             # Bottom right bulge
        (64, 112),                # Bottom point
        (64 - w, 88),             # Bottom left bulge
        (64 - (w*0.5), 64),       # Middle left indent
        (64 - w, 40)              # Top left bulge
    ]
    
    # Draw the Outer Red Glow
    draw.polygon(points, fill=(220, 10, 20, 180))
    
    # Coordinates for the inner dark void (slightly smaller)
    inner_w = w * 0.6
    inner_points = [
        (64, 24),
        (64 + inner_w, 42),
        (64 + (inner_w*0.5), 64),
        (64 + inner_w, 86),
        (64, 104),
        (64 - inner_w, 86),
        (64 - (inner_w*0.5), 64),
        (64 - inner_w, 42)
    ]
    
    # Draw the Inner Black/Dark Red Void
    draw.polygon(inner_points, fill=(5, 0, 10, 255))
    
    # Apply a heavy blur so it looks like raw glowing energy instead of flat shapes
    img = img.filter(ImageFilter.GaussianBlur(radius=4))
    
    filename = f"space_rip_frame_{frame}.png"
    img.save(filename)
    print(f"Generated {filename}")

print("Done! Drop into textures/particle/ folder.")
